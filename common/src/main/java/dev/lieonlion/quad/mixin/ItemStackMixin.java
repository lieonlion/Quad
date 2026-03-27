package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lieonlion.quad.tags.QuadDamageTypeTags;
import dev.lieonlion.quad.tags.QuadItemTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemStack.class, priority = 1004)
public abstract class ItemStackMixin {
    @Shadow public abstract Holder<Item> typeHolder();

    @ModifyReturnValue(method = "canBeHurtBy", at = @At(value = "RETURN"))
    private boolean applyTagsImmuneDamages(boolean original, DamageSource damage) {
        if (typeHolder().is(QuadItemTags.IMMUNE_FIRE) && damage.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } if (typeHolder().is(QuadItemTags.IMMUNE_LIGHTNING) && damage.is(DamageTypeTags.IS_LIGHTNING)) {
            return false;
        } if (typeHolder().is(QuadItemTags.IMMUNE_EXPLOSION) && damage.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        } if (typeHolder().is(QuadItemTags.IMMUNE_CACTUS) && damage.is(QuadDamageTypeTags.IS_CACTUS)) {
            return false;
        } return original;
    }
}
