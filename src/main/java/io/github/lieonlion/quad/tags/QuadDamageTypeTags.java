package io.github.lieonlion.quad.tags;

import io.github.lieonlion.quad.Quad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class QuadDamageTypeTags {
    public static final TagKey<DamageType> IS_CACTUS = createTag("is_cactus");

    private static TagKey<DamageType> createTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, Quad.asId(name));
    }
}