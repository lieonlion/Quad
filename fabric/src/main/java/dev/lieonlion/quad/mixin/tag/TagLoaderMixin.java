package dev.lieonlion.quad.mixin.tag;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import dev.lieonlion.quad.tag.QuadTagRecords;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.DependencySorter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Mixin(TagLoader.class)
public class TagLoaderMixin<T> {
    /*
        This is a ported version of NeoForge's own tag entry removal system to work on Fabric.
        None of this is my own idea / work. All I have done is made it a mixin to work across loaders.
    */

    @Shadow @Final private static Logger LOGGER;
    @Mutable
    @Shadow @Final private String directory;

    @Mutable
    @Shadow
    @Final
    TagLoader.ElementLookup<T> elementLookup;

    public TagLoaderMixin(TagLoader.ElementLookup<T> elementLookup, String directory) {
        this.elementLookup = elementLookup;
        this.directory = directory;
    }

    @Shadow
    private static <T> Map<TagKey<T>, List<Holder<T>>> wrapTags(ResourceKey<? extends Registry<T>> registryKey, Map<ResourceLocation, List<Holder<T>>> tags) {
        return null;
    }

    @Unique
    public Map<ResourceLocation, List<QuadTagRecords.QuadEntryWithSource>> quad$load(ResourceManager resourceManager) {
        Map<ResourceLocation, List<QuadTagRecords.QuadEntryWithSource>> map = Maps.newHashMap();
        FileToIdConverter fileToIdConverter = FileToIdConverter.json(this.directory);

        for (Map.Entry<ResourceLocation, List<Resource>> entry : fileToIdConverter.listMatchingResourceStacks(resourceManager).entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            ResourceLocation resourceLocation2 = fileToIdConverter.fileToId(resourceLocation);

            for (Resource resource : entry.getValue()) {
                try (Reader reader = resource.openAsReader()) {
                    JsonElement jsonElement = JsonParser.parseReader(reader);
                    List<QuadTagRecords.QuadEntryWithSource> list = map.computeIfAbsent(resourceLocation2, resourceLocationx -> new ArrayList<>());
                    QuadTagRecords.QuadTagFile tagFile = QuadTagRecords.QuadTagFile.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement)).getOrThrow();
                    if (tagFile.replace()) {
                        list.clear();
                    }

                    String string = resource.sourcePackId();
                    tagFile.entries().forEach(tagEntry -> list.add(new QuadTagRecords.QuadEntryWithSource(tagEntry, string)));
                    tagFile.remove().forEach(tagEntry -> list.add(new QuadTagRecords.QuadEntryWithSource(tagEntry, string, true)));
                } catch (Exception exception) {
                    LOGGER.error("Couldn't read tag list {} from {} in data pack {}", new Object[]{resourceLocation2, resourceLocation, resource.sourcePackId(), exception});
                }
            }
        }

        return map;
    }

    @Unique
    private Either<List<QuadTagRecords.QuadEntryWithSource>, List<T>> quad$tryBuildTag(TagEntry.Lookup<T> lookup, List<QuadTagRecords.QuadEntryWithSource> entries) {
        SequencedSet<T> sequencedset = new LinkedHashSet<>();
        List<QuadTagRecords.QuadEntryWithSource> list = new ArrayList<>();

        for (QuadTagRecords.QuadEntryWithSource quad$entryWithSource : entries) {
            TagEntry entry = quad$entryWithSource.entry();
            Consumer<T> type;

            if (quad$entryWithSource.remove()) {
                Objects.requireNonNull(sequencedset);
                type = sequencedset::remove;
            } else {
                Objects.requireNonNull(sequencedset);
                type = sequencedset::add;
            }

            if (!entry.build(lookup, type)) {
                list.add(quad$entryWithSource);
            }
        }

        return list.isEmpty() ? Either.right(List.copyOf(sequencedset)) : Either.left(list);
    }

    @Unique
    public Map<ResourceLocation, List<T>> quad$build(Map<ResourceLocation, List<QuadTagRecords.QuadEntryWithSource>> builders) {
        final Map<ResourceLocation, List<T>> map = new HashMap<>();
        TagEntry.Lookup<T> lookup = new TagEntry.Lookup<T>() {
            @Nullable
            public T element(ResourceLocation id, boolean required) {
                return TagLoaderMixin.this.elementLookup.get(id, required).orElse(null);
            }

            @Nullable
            public Collection<T> tag(ResourceLocation p_216041_) {
                return map.get(p_216041_);
            }
        };
        DependencySorter<ResourceLocation, QuadTagRecords.QuadSortingEntry> dependencySorter = new DependencySorter<>();
        builders.forEach((resourceLocation, list) -> dependencySorter.addEntry(resourceLocation, new QuadTagRecords.QuadSortingEntry(list)));
        dependencySorter.orderByDependencies(
            (resourceLocation, sortingEntry) -> this.quad$tryBuildTag(lookup, sortingEntry.entries())
            .ifLeft(
                collection -> LOGGER.error(
                    "Couldn't load tag {} as it is missing following references: {}",
                    resourceLocation,
                    Collectors.joining("\n\t", "\n\t", "")
                )
            )
            .ifRight((collection) -> map.put(resourceLocation, collection))
        );
        return map;
    }

    @Inject(method = "loadTagsForRegistry", at = @At(value = "HEAD"), cancellable = true)
    private static <T> void loadTagsForRegistry(ResourceManager resourceManager, WritableRegistry<T> registry, CallbackInfo ci) {
        ci.cancel();
        ResourceKey<? extends Registry<T>> resourceKey = registry.key();
        TagLoaderMixin<Holder<T>> tagLoader = new TagLoaderMixin<>(TagLoader.ElementLookup.fromWritableRegistry(registry), Registries.tagsDirPath(resourceKey));
        tagLoader.quad$build(tagLoader.quad$load(resourceManager)).forEach(
            (resourceLocation, list) -> registry.bindTag(TagKey.create(resourceKey, resourceLocation), list)
        );
    }

    @Inject(method = "loadPendingTags", at = @At(value = "HEAD"), cancellable = true)
    private static <T> void loadPendingTags(ResourceManager resourceManager, Registry<T> registry, CallbackInfoReturnable<Optional<Registry.PendingTags<T>>> cir) {
        cir.cancel();
        ResourceKey<? extends Registry<T>> resourceKey = registry.key();
        TagLoaderMixin<Holder<T>> tagLoader = new TagLoaderMixin<>(TagLoader.ElementLookup.fromFrozenRegistry(registry), Registries.tagsDirPath(resourceKey));
        TagLoader.LoadResult<T> loadresult = new TagLoader.LoadResult<T>(resourceKey, wrapTags(registry.key(), tagLoader.quad$build(tagLoader.quad$load(resourceManager))));
        cir.setReturnValue(loadresult.tags().isEmpty() ? Optional.empty() : Optional.of(registry.prepareTagReload(loadresult)));
    }
}
