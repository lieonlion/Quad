package dev.lieonlion.quad.tag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.util.DependencySorter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class QuadTagRecords {
    public record QuadTagFile(List<TagEntry> entries, List<TagEntry> remove, boolean replace) {
        public static final Codec<QuadTagFile> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                TagEntry.CODEC.listOf().fieldOf("values").forGetter(QuadTagFile::entries), TagEntry.CODEC.listOf().optionalFieldOf("remove", List.of()).forGetter(QuadTagFile::remove), Codec.BOOL.optionalFieldOf("replace", false).forGetter(QuadTagFile::replace)
            ).apply(instance, QuadTagFile::new)
        );
    }

    public record QuadEntryWithSource(TagEntry entry, String source, boolean remove) {
        public QuadEntryWithSource(TagEntry entry, String source) {
            this(entry, source, false);
        }

        public @NotNull String toString() {
            return this.entry.toString() + " (from " + this.source + ")";
        }
    }

    public record QuadSortingEntry(List<QuadEntryWithSource> entries) implements DependencySorter.Entry<ResourceLocation> {
        @Override
        public void visitRequiredDependencies(Consumer<ResourceLocation> visitor) {
            this.entries.forEach(entryWithSource -> entryWithSource.entry.visitRequiredDependencies(visitor));
        }

        @Override
        public void visitOptionalDependencies(Consumer<ResourceLocation> visitor) {
            this.entries.forEach(entryWithSource -> entryWithSource.entry.visitOptionalDependencies(visitor));
        }
    }
}
