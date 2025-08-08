package dev.lieonlion.quad;

import dev.lieonlion.quad.platform.Services;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quad {
    public static final String MOD_ID = "quad";
    public static final String MOD_NAME = "Quad";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOG.info("[Quad] Innitialising the Quad mod power running on {}! >:P", Services.PLATFORM.getPlatformName());
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
