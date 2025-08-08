package dev.lieonlion.quad.tags;

import dev.lieonlion.quad.Quad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class QuadItemTags {
    public static final TagKey<Item> FUEL_LAVA = create("fuel/lava"); //20000
    public static final TagKey<Item> FUEL_COAL_BLOCK = create("fuel/coal_block"); //16000
    public static final TagKey<Item> FUEL_DRIED_KELP_BLOCK = create("fuel/dried_kelp_block"); //4001
    public static final TagKey<Item> FUEL_BLAZE_ROD = create("fuel/blaze_rod"); //2400
    public static final TagKey<Item> FUEL_COAL = create("fuel/coal"); //1600
    public static final TagKey<Item> FUEL_BOAT = create("fuel/boat"); //1200
    public static final TagKey<Item> FUEL_HANGING_SIGN = create("fuel/hanging_sign"); //800
    public static final TagKey<Item> FUEL_WOOD = create("fuel/wood"); //300
    public static final TagKey<Item> FUEL_WOOD_TOOL = create("fuel/wood_tool"); //200
    public static final TagKey<Item> FUEL_WOOD_SLAB = create("fuel/wood_slab"); //150
    public static final TagKey<Item> FUEL_WOOL = create("fuel/wool"); //100
    public static final TagKey<Item> FUEL_CARPET = create("fuel/carpet"); //67
    public static final TagKey<Item> FUEL_BAMBOO = create("fuel/bamboo"); //50

    public static final TagKey<Item> IMMUNE_CACTUS = create("immune/cactus");
    public static final TagKey<Item> IMMUNE_EXPLOSION = create("immune/explosion");
    public static final TagKey<Item> IMMUNE_FIRE = create("immune/fire");
    public static final TagKey<Item> IMMUNE_LIGHTNING = create("immune/lightning");

    public static final TagKey<Item> PROTECTS_FROM_BURNS = create("protects_from/burns");

    public static final TagKey<Item> SNOW_ACTS_SOLID = create("snow/acts_solid");
    public static final TagKey<Item> SNOW_BOOTS = create("snow/boots");

    @Deprecated // use `minecraft:gaze_disguise_equipment`
    public static final TagKey<Item> PACIFIER_ENDERMAN = create("pacifier/enderman");
    @Deprecated // use `minecraft:piglin_safe_armor`
    public static final TagKey<Item> PACIFIER_PIGLIN = create("pacifier/piglin");

    public static final TagKey<Item> NEVER_DESPAWN = create("never_despawn");
    public static final TagKey<Item> NO_GRAVITY = create("no_gravity");
    public static final TagKey<Item> IRON_GOLEM_HEALER = create("iron_golem_healer");
    public static final TagKey<Item> FIRE_LIGHTER = create("fire_lighter");
    public static final TagKey<Item> RESPAWN_ANCHOR_CHARGER = create("respawn_anchor_charger");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Quad.location(name));
    }
}
