package dev.lieonlion.quad;

import dev.lieonlion.quad.registry.FabricQuadFuelRegistry;
import dev.lieonlion.quad.tags.QuadItemTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.TntBlock;

public class FabricQuad implements ModInitializer {
    @Override
    public void onInitialize() {
        Quad.init();

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity && !world.isClientSide) {
                if (itemEntity.getItem().is(QuadItemTags.NEVER_DESPAWN)) {
                    itemEntity.setUnlimitedLifetime();
                } if (itemEntity.getItem().is(QuadItemTags.NO_GRAVITY)) {
                    itemEntity.setNoGravity(true);
                } else if (itemEntity.isNoGravity()) {
                    itemEntity.setNoGravity(false);
                }
            }
        });

        FuelRegistryEvents.BUILD.register(FabricQuadFuelRegistry::registerQuadFuelItems);
    }
}
