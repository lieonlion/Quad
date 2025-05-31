package io.github.lieonlion.quad.replacement;

import io.github.lieonlion.quad.util.QuadResourceUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlockReplacement {
    public static final Map<String, List<BlockReplacementRecord>> REPLACEMENT = new HashMap<>();
    public static final Map<Block, Block> FALLBACK = new HashMap<>();

    private static Structure structure;

    public static BlockState getBlockReplacement(ServerLevelAccessor accessor, BlockState state) {
//        if (state.isAir()) return state;
//
//        Registry<Structure> structureRegistry = accessor.registryAccess().registryOrThrow(Registries.STRUCTURE);
//        List<BlockReplacementRecord> structureEntry = REPLACEMENT.get(Objects.requireNonNull(structureRegistry.getKey(structure)).toString());
//
//        if (structureEntry.isEmpty()) {
//            REPLACEMENT.keySet().stream().filter(blockReplacementRecords -> blockReplacementRecords.startsWith("#")).forEach(s -> {
//                if (structureRegistry.getHolder(structureRegistry.getKey(structure)).get().is(TagKey.create(Registries.STRUCTURE, ResourceLocation.tryParse(s)))) {
//                    return REPLACEMENT.get("#" + s);
//                }
//            });
//        }

        return state;


//        Optional<ResourceLocation> structureOptional = accessor.registryAccess().registry(Registries.STRUCTURE).map(it -> it.getKey(structure));
//
//        if (structureOptional.isEmpty())
//            return state;
//
//        ResourceLocation structure_id = structureOptional.get();
//        List<BlockReplacementRecord> entries = REPLACEMENT.get(structure_id);
//
//        if (entries != null) {
//            for (var entry : entries) {
//                if (entry.targets.contains(state.getBlock())) {
//                    return entry.replacement.withPropertiesOf(state);
//                }
//            }
//        } return FALLBACK.getOrDefault(state.getBlock(), state.getBlock()).withPropertiesOf(state);
    }

    public static void pushToReplacement(ResourceLocation structure, List<BlockReplacementRecord> blockReplacementsList) {
//        if (blockReplacementsList.isEmpty())
//            return;
//
//        List<BlockReplacementRecord> structureReplacementsList = REPLACEMENT.get(structure);
//
//        if (structureReplacementsList != null) {
//            for (var blockReplacement : blockReplacementsList) {
//                for (var structureReplacement : REPLACEMENT.get(structure)) {
//                    if (blockReplacement.replacement == structureReplacement.replacement) {
//                        structureReplacement.targets.forEach(block -> {
//                            if (!blockReplacement.targets.contains(block)) blockReplacement.targets.add(block);
//                        });
//                    }
//                }
//            } structureReplacementsList.forEach(blockReplacement -> {
//                if (!blockReplacementsList.contains(blockReplacement)) {
//                    blockReplacementsList.add(blockReplacement);
//                }
//            });
//        }
//
//        Quad.LOGGER.info("[Quad] The Structure array (keep an eye on this): {}", structureReplacementsList);
//
//        REPLACEMENT.put(structure, blockReplacementsList);
    }

    public static void pushToFallback(List<BlockReplacementRecord> blockReplacementsList) {
//        if (blockReplacementsList.isEmpty())
//            return;
//        for (var blockReplacement : blockReplacementsList) {
//            for (var block : blockReplacement.targets) {
//                FALLBACK.put(block, blockReplacement.replacement);
//            }
//        }
    }

    public static void pushStructure(Structure structure) {
        BlockReplacement.structure = structure;
    }

    public record BlockReplacementRecord(List<String> targets, String replacement) {}
}
