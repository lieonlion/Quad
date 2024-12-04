package io.github.lieonlion.quad.tags;

import io.github.lieonlion.quad.Quad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class QuadItemTags {
    public static final TagKey<Item> FUEL_LAVA = createTag("fuel/lava"); //20000
    public static final TagKey<Item> FUEL_COAL_BLOCK = createTag("fuel/coal_block"); //16000
    public static final TagKey<Item> FUEL_DRIED_KELP_BLOCK = createTag("fuel/dried_kelp_block"); //4001
    public static final TagKey<Item> FUEL_BLAZE_ROD = createTag("fuel/blaze_rod"); //2400
    public static final TagKey<Item> FUEL_COAL = createTag("fuel/coal"); //1600
    public static final TagKey<Item> FUEL_BOAT = createTag("fuel/boat"); //1200
    public static final TagKey<Item> FUEL_HANGING_SIGN = createTag("fuel/hanging_sign"); //800
    public static final TagKey<Item> FUEL_WOOD = createTag("fuel/wood"); //300
    public static final TagKey<Item> FUEL_WOOD_TOOL = createTag("fuel/wood_tool"); //200
    public static final TagKey<Item> FUEL_WOOD_SLAB = createTag("fuel/wood_slab"); //150
    public static final TagKey<Item> FUEL_WOOL = createTag("fuel/wool"); //100
    public static final TagKey<Item> FUEL_CARPET = createTag("fuel/carpet"); //67
    public static final TagKey<Item> FUEL_BAMBOO = createTag("fuel/bamboo"); //50

    public static final TagKey<Item> IMMUNE_CACTUS = createTag("immune/cactus");
    public static final TagKey<Item> IMMUNE_EXPLOSION = createTag("immune/explosion");
    public static final TagKey<Item> IMMUNE_FIRE = createTag("immune/fire");
    public static final TagKey<Item> IMMUNE_LIGHTNING = createTag("immune/lightning");

    public static final TagKey<Item> PROTECTS_FROM_BURNS = createTag("protects_from/burns");

    public static final TagKey<Item> SNOW_ACTS_SOLID = createTag("snow/acts_solid");
    public static final TagKey<Item> SNOW_BOOTS = createTag("snow/boots");

    public static final TagKey<Item> PACIFIER_ENDERMAN = createTag("pacifier/enderman");
    public static final TagKey<Item> PACIFIER_PIGLIN = createTag("pacifier/piglin");

    public static final TagKey<Item> NEVER_DESPAWN = createTag("never_despawn");
    public static final TagKey<Item> NO_GRAVITY = createTag("no_gravity");
    public static final TagKey<Item> IRON_GOLEM_HEALER = createTag("iron_golem_healer");
    public static final TagKey<Item> FIRE_LIGHTER = createTag("fire_lighter");
    public static final TagKey<Item> RESPAWN_ANCHOR_CHARGER = createTag("respawn_anchor_charger");

    private static TagKey<Item> createTag(String id) {
        return TagKey.create(Registries.ITEM, Quad.asId(id));
    }
}