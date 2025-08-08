package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ConduitBlockEntity.class, priority = 1004)
public abstract class NeoConduitBlockEntityMixin {
    @WrapOperation(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isConduitFrame(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean isConduitBaseBlock(BlockState instance, LevelReader levelReader, BlockPos blockPos1, BlockPos blockPos2, Operation<Boolean> original) {
        return (original.call(instance, levelReader, blockPos1, blockPos2) && instance.is(QuadBlockTags.CONDUIT_BASE_BLOCKS)) ||
                instance.is(QuadBlockTags.CONDUIT_BASE_BLOCKS);
    }
}
