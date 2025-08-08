package dev.lieonlion.quad.mixin;

import dev.lieonlion.quad.util.QuadUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TntBlock.class, priority = 1004)
public abstract class FabricTntBlockMixin extends Block {
    public FabricTntBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow private static boolean prime(Level level, BlockPos pos, @Nullable LivingEntity livingEntity) {return false;}

    @Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
    private void applyTagFireLighters(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
        cir.cancel();
        if (QuadUtil.isFireLighter(stack)) {
            if (prime(level, pos, player)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                QuadUtil.usedFireLighter(level, pos, player, hand, stack);
            } else if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)) {
                player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
                cir.setReturnValue(InteractionResult.PASS);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else {
            cir.setReturnValue(super.useItemOn(stack, state, level, pos, player, hand, result));
        }
    }
}