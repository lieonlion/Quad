package dev.lieonlion.quad.tags;

import dev.lieonlion.quad.Quad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

@Deprecated
public class QuadDamageTypeTags {
    @Deprecated
    public static final TagKey<DamageType> IS_CACTUS = create("is_cactus");

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Quad.location(name));
    }
}
