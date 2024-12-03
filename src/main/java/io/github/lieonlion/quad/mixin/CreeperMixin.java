package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.lieonlion.quad.util.QuadUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Creeper.class, priority = 1004)
public abstract class CreeperMixin extends Entity {
    public CreeperMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow public abstract void ignite();

    @Shadow public abstract boolean isIgnited();

    @ModifyReturnValue(method = "mobInteract", at = @At(value = "RETURN"))
    private InteractionResult applyTagFireLighters(InteractionResult original, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (QuadUtil.isFireLighter(stack) && !this.isIgnited()) {
            this.ignite();
            QuadUtil.usedFireLighter(this.level(), new BlockPos((int)this.getX(), (int)this.getY(), (int)this.getZ()), player, hand, stack);
            return InteractionResult.SUCCESS_SERVER;
        } return original;
    }
}