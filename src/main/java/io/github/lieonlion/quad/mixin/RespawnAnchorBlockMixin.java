package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.lieonlion.quad.tags.QuadItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RespawnAnchorBlock.class, priority = 1004)
public abstract class RespawnAnchorBlockMixin {
    @ModifyReturnValue(method = "isRespawnFuel", at = @At(value = "RETURN"))
    private static boolean applyTagRespawnAnchorCharger(boolean original, ItemStack stack) {
        return (original && stack.is(QuadItemTags.RESPAWN_ANCHOR_CHARGER))
                || stack.is(QuadItemTags.RESPAWN_ANCHOR_CHARGER);
    }
}