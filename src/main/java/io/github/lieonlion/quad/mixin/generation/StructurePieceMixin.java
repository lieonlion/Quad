package io.github.lieonlion.quad.mixin.generation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.lieonlion.quad.replacement.BlockReplacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StructurePiece.class, priority = 1100)
public abstract class StructurePieceMixin {
    @WrapOperation(method = "createChest(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean replaceChestState(ServerLevelAccessor instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        state = BlockReplacement.getBlockReplacement(instance, state);
        return original.call(instance, pos, state, i);
    }

    @WrapOperation(method = "placeBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean replaceBlockState(WorldGenLevel instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        state = BlockReplacement.getBlockReplacement(instance, state);
        return original.call(instance, pos, state, i);
    }
}