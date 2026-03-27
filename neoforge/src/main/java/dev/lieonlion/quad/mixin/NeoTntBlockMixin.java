package dev.lieonlion.quad.mixin;

import dev.lieonlion.quad.util.QuadUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TntBlock.class, priority = 1004)
public abstract class NeoTntBlockMixin extends Block {
    @Shadow public abstract boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter);

    public NeoTntBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
    private void applyTagFireLighters(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        cir.cancel();
        if (QuadUtil.isFireLighter(stack)) {
            if (onCaughtFire(state, level, pos, result.getDirection(), player)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                QuadUtil.usedFireLighter(level, pos, player, hand, stack);
            } else if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
                player.sendOverlayMessage(Component.translatable("block.minecraft.tnt.disabled"));
                cir.setReturnValue(InteractionResult.PASS);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else {
            cir.setReturnValue(super.useItemOn(stack, state, level, pos, player, hand, result));
        }
    }
}