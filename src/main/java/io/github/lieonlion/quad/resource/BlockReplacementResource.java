package io.github.lieonlion.quad.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.github.lieonlion.quad.Quad;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class BlockReplacementResource extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final String ID = "block_replacement";
    private static final Gson GSON = new GsonBuilder().create();

    public static BlockReplacementResource INSTANCE = new BlockReplacementResource();

    public BlockReplacementResource() {
        super(GSON, ID);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

    }

    @Override
    public ResourceLocation getFabricId() {
        return Quad.asId(ID);
    }
}
