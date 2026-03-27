package dev.lieonlion.quad.tags;

import dev.lieonlion.quad.Quad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class QuadBlockTags {
    public static final TagKey<Block> CATS_ON_BLOCKS_SIT = create("cats_on_blocks/sit");
    public static final TagKey<Block> CATS_ON_BLOCKS_LIE = create("cats_on_blocks/lie");

    public static final TagKey<Block> NETHER_PORTAL_BUILT = create("nether_portal/built");
    public static final TagKey<Block> NETHER_PORTAL_FORMED = create("nether_portal/formed");

    public static final TagKey<Block> CONDUIT_BASE_BLOCKS = create("conduit_base_blocks");

  public static final TagKey<Block> COPPER_GOLEM_ITEM_DESTINATION = create("copper_golem_item_destination");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Quad.location(name));
    }
}
