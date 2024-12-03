package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ConduitBlockEntity.class, priority = 1004)
public abstract class ConduitBlockEntityMixin {
    @WrapOperation(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean isConduitBaseBlock(BlockState instance, Block block, Operation<Boolean> original) {
        return (original.call(instance, block) && instance.is(QuadBlockTags.CONDUIT_BASE_BLOCKS)) ||
                instance.is(QuadBlockTags.CONDUIT_BASE_BLOCKS);
    }
}
