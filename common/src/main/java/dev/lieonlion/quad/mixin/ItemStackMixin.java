package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lieonlion.quad.tags.QuadDamageTypeTags;
import dev.lieonlion.quad.tags.QuadItemTags;
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
    @Shadow public abstract boolean is(TagKey<Item> tagKey);

    @ModifyReturnValue(method = "canBeHurtBy", at = @At(value = "RETURN"))
    private boolean applyTagsImmuneDamages(boolean original, DamageSource damageSource) {
        if (is(QuadItemTags.IMMUNE_FIRE) && damageSource.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } if (is(QuadItemTags.IMMUNE_LIGHTNING) && damageSource.is(DamageTypeTags.IS_LIGHTNING)) {
            return false;
        } if (is(QuadItemTags.IMMUNE_EXPLOSION) && damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        } if (is(QuadItemTags.IMMUNE_CACTUS) && damageSource.is(QuadDamageTypeTags.IS_CACTUS)) {
            return false;
        } return original;
    }
}