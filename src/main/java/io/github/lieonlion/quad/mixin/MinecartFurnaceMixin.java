package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.lieonlion.quad.Quad;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecartFurnace.class, priority = 1004)
public abstract class MinecartFurnaceMixin {
    @Shadow private int fuel;

    @Inject(method = "interact", at = @At(value = "HEAD"))
    private void applyAbstractFurnaceFuelItems(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) return;
        int itemFuelTime = player.level().fuelValues().burnDuration(itemStack);
        Quad.LOGGER.info("[Quad] item has fuel time of: {}", itemFuelTime);
        if (itemFuelTime > 0 && this.fuel + itemFuelTime <= 32000) {
            this.fuel += itemFuelTime;
            if (!player.isCreative()) {
                ItemStack itemRemainder = itemStack.getItem().getCraftingRemainder();
                if (!itemRemainder.isEmpty()) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, itemRemainder);
                    player.setItemInHand(hand, itemStack2);
                } else {
                    itemStack.shrink(1);
                }
            }
        } Quad.LOGGER.info("[Quad] cart fuel: {}", this.fuel);
    }

    @ModifyExpressionValue(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean ignoreIngredient(boolean original) {
        return false;
    }
}