package io.github.lieonlion.quad.mixin;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Stream;

@Mixin(TagLoader.class)
public class TagLoaderMixin {

    @Unique Map<ResourceLocation, ResourceLocation[]> quad$parentTagToExcludedIds = Maps.newHashMap();
    @Unique Map<ResourceLocation, Set<TagKey<Block>>> quad$parentTagToExcludedBlockTags = Maps.newHashMap();
    @Unique Map<ResourceLocation, Set<TagKey<Item>>> quad$parentTagToExcludedItemTags = Maps.newHashMap();
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;", shift = At.Shift.BY, by = 2))
    private void injectedLoad(ResourceManager resourceManager, CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir, @Local(ordinal = 0) ResourceLocation resourceLocation, @Local(ordinal = 0) JsonElement jsonElement) {

        jsonElement.getAsJsonObject().get("values").getAsJsonArray().forEach(tagEntry -> { if(tagEntry.isJsonObject())
            {
                JsonElement excludeBool = tagEntry.getAsJsonObject().get("exclude");
                boolean exclude = excludeBool != null && excludeBool.isJsonPrimitive() && excludeBool.getAsBoolean();
                if (exclude) {
                    ResourceLocation parentTag = ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), resourceLocation.getPath().replace(".json", "").replaceFirst("tags/block[s]?/", "").replaceFirst("tags/item[s]?/", ""));
                    String excludedString = tagEntry.getAsJsonObject().get("id").getAsString();
                    if (excludedString.startsWith("#")) {
                        String tagTypeName = resourceLocation.getPath().split("tags/")[1].split("/")[0];
                        excludedString = excludedString.substring(1);
                        switch (tagTypeName) {
                            case "block", "blocks": {
                                TagKey<Block> excludedTagKey = TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(excludedString));
                                if (quad$parentTagToExcludedBlockTags.containsKey(parentTag)) {
                                    Set<TagKey<Block>> tagSet = quad$parentTagToExcludedBlockTags.get(parentTag);
                                    tagSet.add(excludedTagKey);
                                    quad$parentTagToExcludedBlockTags.put(parentTag, tagSet);
                                } else {
                                    quad$parentTagToExcludedBlockTags.put(parentTag, Set.of(excludedTagKey));
                                }
                                break;
                            }
                            case "item", "items":
                            case null:
                            default: {
                                TagKey<Item> excludedTagKey = TagKey.create(Registries.ITEM, ResourceLocation.tryParse(excludedString));
                                if (quad$parentTagToExcludedItemTags.containsKey(parentTag)) {
                                    Set<TagKey<Item>> tagSet = quad$parentTagToExcludedItemTags.get(parentTag);
                                    tagSet.add(excludedTagKey);
                                    quad$parentTagToExcludedItemTags.put(parentTag, tagSet);
                                } else {
                                    quad$parentTagToExcludedItemTags.put(parentTag, Set.of(excludedTagKey));
                                }
                                break;
                            }
                        }
                    } else {
                        ResourceLocation excludedId = ResourceLocation.tryParse(tagEntry.getAsJsonObject().get("id").getAsString());
                        LOGGER.info("Removing " + excludedId.toString() + " from " + parentTag);
                        if (quad$parentTagToExcludedIds.containsKey(parentTag)) {
                            quad$parentTagToExcludedIds.put(parentTag, Stream.concat(Arrays.stream(quad$parentTagToExcludedIds.get(parentTag)), Stream.of(excludedId))
                                    .toArray(ResourceLocation[]::new));
                        } else {
                            quad$parentTagToExcludedIds.put(parentTag, new ResourceLocation[]{excludedId});
                        }
                    }
                }
            }
        });
    }

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At(value = "RETURN"), cancellable = true)
    private void injectedBuildAtReturn(Map<ResourceLocation, List<TagLoader.EntryWithSource>> map, CallbackInfoReturnable<Map<ResourceLocation, Collection<?>>> cir) {
        Map<ResourceLocation, Collection<?>> original = cir.getReturnValue();
        HashMap<ResourceLocation, Collection<?>> filteredMap = new HashMap<>(original);
        for (Map.Entry<ResourceLocation, Collection<?>> entry : original.entrySet()) {
            // Exclude by IDs
            if (quad$parentTagToExcludedIds.containsKey(entry.getKey())) {
                Set<ResourceLocation> excludedIds = Set.of(quad$parentTagToExcludedIds.get(entry.getKey()));
                LOGGER.info("Found " + entry.getKey() + " in List (IDs)");
                Collection<? extends Holder.Reference<?>> filtered = entry.getValue().stream()
                        .map(holder -> (Holder.Reference<?>) holder)
                        .filter(ref -> excludedIds.stream().noneMatch(ref::is))
                        .toList();
                filteredMap.put(entry.getKey(), filtered);
                LOGGER.info("Filtered Value (IDs): " + filtered);
            }
            boolean isBlockEntry = false;
            boolean isItemEntry = false;
            if (!entry.getValue().isEmpty()) {
                Object first = entry.getValue().iterator().next();
                if (first instanceof Holder.Reference<?>) {
                    if (((Holder.Reference<?>) first).value() instanceof Block) {
                        isBlockEntry = true;
                    }
                    if (((Holder.Reference<?>) first).value() instanceof Item) {
                        isItemEntry = true;
                    }
                }
            }
            // Exclude by Block Tags
            if (isBlockEntry) {
                if (quad$parentTagToExcludedBlockTags.containsKey(entry.getKey())) {
                    Set<TagKey<Block>> excludedTags = quad$parentTagToExcludedBlockTags.get(entry.getKey());
                    LOGGER.info("Found {} in List (Block Tags): {}", entry.getKey(), excludedTags);

                    // Collect all Blocks from excluded Block tags
                    Set<ResourceKey<Block>> excludedBlockKeys = new HashSet<>();
                    for (TagKey<Block> tagKey : excludedTags) {
                        Collection<?> tagBlocks = original.get(tagKey.location());
                        if (tagBlocks != null) {
                            for (Object holder : tagBlocks) {
                                if (holder instanceof Holder.Reference<?> ref) {
                                    @SuppressWarnings("unchecked")
                                    Optional<ResourceKey<Block>> key = ((Holder.Reference<Block>) ref).unwrapKey();
                                    key.ifPresent(excludedBlockKeys::add);
                                }
                            }
                        }
                    }

                    @SuppressWarnings("unchecked")
                    Collection<Holder.Reference<Block>> filtered = filteredMap.get(entry.getKey()).stream()
                            .map(holder -> (Holder.Reference<Block>) holder)
                            .filter(ref -> ref.unwrapKey().map(key -> !excludedBlockKeys.contains(key)).orElse(true))
                            .toList();
                    filteredMap.put(entry.getKey(), filtered);
                    LOGGER.info("Filtered Value (Block Tags): {}", filtered);
                }
            }
            // Exclude by Item Tags
            if (isItemEntry) {
                if (quad$parentTagToExcludedItemTags.containsKey(entry.getKey())) {
                    Set<TagKey<Item>> excludedItemTags = quad$parentTagToExcludedItemTags.get(entry.getKey());
                    LOGGER.info("Found {} in List (Item Tags): {}", entry.getKey(), excludedItemTags);

                    // Collect all Items from excluded Item tags
                    Set<ResourceKey<Item>> excludedItemKeys = new HashSet<>();
                    for (TagKey<Item> tagKey : excludedItemTags) {
                        Collection<?> tagItems = original.get(tagKey.location());
                        if (tagItems != null) {
                            for (Object holder : tagItems) {
                                if (holder instanceof Holder.Reference<?> ref) {
                                    @SuppressWarnings("unchecked")
                                    Optional<ResourceKey<Item>> key = ((Holder.Reference<Item>) ref).unwrapKey();
                                    key.ifPresent(excludedItemKeys::add);
                                }
                            }
                        }
                    }

                    @SuppressWarnings("unchecked")
                    Collection<Holder.Reference<Item>> filtered = filteredMap.get(entry.getKey()).stream()
                            .map(holder -> (Holder.Reference<Item>) holder)
                            .filter(ref -> ref.unwrapKey().map(key -> !excludedItemKeys.contains(key)).orElse(true))
                            .toList();
                    filteredMap.put(entry.getKey(), filtered);
                    LOGGER.info("Filtered Value (Item Tags): {}", filtered);
                }
            }
        }
        cir.setReturnValue(filteredMap);
    }
}
