package io.github.lieonlion.quad;

import io.github.lieonlion.quad.registry.QuadFuelRegistry;
import io.github.lieonlion.quad.tags.QuadItemTags;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.world.entity.item.ItemEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quad implements ModInitializer {
	public static final String MODID = "quad";
	public static final Logger LOGGER = LoggerFactory.getLogger("Quad");

	@Override
	public void onInitialize() {
		LOGGER.info("[Quad] Initialising the Quad mod power! >:P");

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

		FuelRegistryEvents.BUILD.register(QuadFuelRegistry::registerFuel);
	}

	public static ResourceLocation asId(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}