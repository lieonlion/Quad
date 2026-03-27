package dev.lieonlion.quad.registry;

import dev.lieonlion.quad.Quad;
import dev.lieonlion.quad.tags.QuadItemTags;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.entity.FuelValues;

public class FabricQuadFuelRegistry {
    public static void registerQuadFuelItems(FuelValues.Builder builder, FuelValueEvents.Context context) {
        int i = context.baseSmeltTime();
        builder.add(QuadItemTags.FUEL_BAMBOO, i / 4)
                .add(QuadItemTags.FUEL_CARPET, 1 + i / 3)
                .add(QuadItemTags.FUEL_WOOL, i / 2)
                .add(QuadItemTags.FUEL_WOOD_SLAB, i * 3 / 4)
                .add(QuadItemTags.FUEL_WOOD_TOOL, i)
                .add(QuadItemTags.FUEL_WOOD, i * 3 / 2)
                .add(QuadItemTags.FUEL_HANGING_SIGN, i * 4)
                .add(QuadItemTags.FUEL_BOAT, i * 6)
                .add(QuadItemTags.FUEL_COAL, i * 8)
                .add(QuadItemTags.FUEL_BLAZE_ROD, i * 12)
                .add(QuadItemTags.FUEL_DRIED_KELP_BLOCK, 1 + i * 20)
                .add(QuadItemTags.FUEL_COAL_BLOCK, i * 8 * 10)
                .add(QuadItemTags.FUEL_LAVA, i * 100)
                .remove(ItemTags.NON_FLAMMABLE_WOOD);
        Quad.LOG.info("[Quad] loaded quad fuel tags");
    }
}