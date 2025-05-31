package io.github.lieonlion.quad.replacement;

import com.google.gson.*;
import io.github.lieonlion.quad.Quad;
import io.github.lieonlion.quad.util.QuadResourceUtil;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockReplacementResource extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final String ID = "block_replacement";
    private static final Gson GSON = new GsonBuilder().create();
    private static MinecraftServer level;

    public static BlockReplacementResource INSTANCE = new BlockReplacementResource();

    public BlockReplacementResource() {
        super(GSON, ID);
    }

    public void setLevel(MinecraftServer level) {
        Quad.LOGGER.info("set level");
        BlockReplacementResource.level = level;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        List<String> namespacesWithResource = new ArrayList<>();

        object.forEach((key, value) -> {
            if (!namespacesWithResource.contains(key.getNamespace())) namespacesWithResource.add(key.getNamespace());
            try {
                JsonObject file = value.getAsJsonObject();
                JsonElement blocks = file.get("blocks");
                Quad.LOGGER.info(QuadResourceUtil.getElementsAsListUnpackedTags(blocks, Registries.BLOCK, level).toString());
            } catch(Exception e) {
                Quad.LOGGER.error("[Quad] failed to load block replacement from file: {} due to: {}", key, e);
            }
        });

        Quad.LOGGER.info("[Quad] found {} block replacement file(s) from namespace(s): {}", object.size(), namespacesWithResource);
    }

    @Override
    public ResourceLocation getFabricId() {
        return Quad.asId(ID);
    }
}
