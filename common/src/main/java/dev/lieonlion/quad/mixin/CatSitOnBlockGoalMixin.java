package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CatSitOnBlockGoal.class, priority = 1004)
public abstract class CatSitOnBlockGoalMixin {
    @WrapOperation(method = "isValidTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean applyTagCatsOnBlocksSit1(BlockState instance, Block block, Operation<Boolean> original) {
        return (original.call(instance, block) && instance.is(QuadBlockTags.CATS_ON_BLOCKS_SIT))
                || (instance.getBlock() instanceof ChestBlock && instance.is(QuadBlockTags.CATS_ON_BLOCKS_SIT));
    }

    @WrapOperation(method = "isValidTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean applyTagCatsOnBlocksSit2(BlockState instance, Block block, Operation<Boolean> original) {
        return (original.call(instance, block) && instance.is(QuadBlockTags.CATS_ON_BLOCKS_SIT))
                || (instance.getBlock() instanceof AbstractFurnaceBlock && instance.is(QuadBlockTags.CATS_ON_BLOCKS_SIT));
    }

    @ModifyReturnValue(method = "isValidTarget", at = @At(value = "RETURN"))
    private boolean applyTagCatsOnBlocksSit3(boolean original, LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return original || (level.isEmptyBlock(pos.above()) && state.is(QuadBlockTags.CATS_ON_BLOCKS_SIT)
                && !(state.getBlock() instanceof AbstractFurnaceBlock) && !(state.getBlock() instanceof ChestBlock));
    }
}