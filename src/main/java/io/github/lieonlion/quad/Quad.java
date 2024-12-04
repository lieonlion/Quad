package io.github.lieonlion.quad;

import io.github.lieonlion.quad.registry.QuadFuelRegistry;
import io.github.lieonlion.quad.tags.QuadItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Quad.MODID)
public class Quad {
    public static final String MODID = "quad";
    public static final Logger LOGGER = LoggerFactory.getLogger("Quad");

    public Quad() {
        LOGGER.info("[Quad] Initialising the Quad mod power! >:P");

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

        //(neo)forge event for items fuel times
        NeoForge.EVENT_BUS.addListener(QuadFuelRegistry::onFurnaceFuelBurnTime);
    }

    public static ResourceLocation asId(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }
}