package dev.lieonlion.quad;

import dev.lieonlion.quad.registry.NeoQuadFuelRegistry;
import dev.lieonlion.quad.tags.QuadItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(Quad.MOD_ID)
public class NeoQuad {
    public NeoQuad(IEventBus eventBus) {
        Quad.init();

        NeoForge.EVENT_BUS.addListener((EntityJoinLevelEvent event) -> {
            if (event.getEntity() instanceof ItemEntity itemEntity) {
                if (itemEntity.getItem().is(QuadItemTags.NEVER_DESPAWN)) {
                    itemEntity.setUnlimitedLifetime();
                } if (itemEntity.getItem().is(QuadItemTags.NO_GRAVITY)) {
                    itemEntity.setNoGravity(true);
                } else if (itemEntity.isNoGravity()) {
                    itemEntity.setNoGravity(false);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener(NeoQuadFuelRegistry::registerQuadFuelItems);
    }
}