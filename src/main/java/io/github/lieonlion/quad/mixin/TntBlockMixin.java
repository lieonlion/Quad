package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.lieonlion.quad.util.QuadUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TntBlock.class, priority = 1004)
public abstract class TntBlockMixin {
    @Shadow public abstract void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter);

    @ModifyReturnValue(method = "useItemOn", at = @At(value = "RETURN"))
    private InteractionResult applyTagFireLighters(InteractionResult original, ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (QuadUtil.isFireLighter(stack)) {
            this.onCaughtFire(state, level, pos, hit.getDirection(), player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            QuadUtil.usedFireLighter(level, state, pos, player, hand, stack);
            return InteractionResult.SUCCESS_SERVER;
        } return original;
    }

    @ModifyExpressionValue(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean ignoreStack(boolean original) {
        return false;
    }
}