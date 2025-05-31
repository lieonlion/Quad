package io.github.lieonlion.quad.mixin.generation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.lieonlion.quad.replacement.BlockReplacement;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(value = StructureTemplate.class, priority = 1100)
public abstract class StructureTemplateMixin {
    @WrapOperation(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;rotate(Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState blockStateModify(BlockState instance, Rotation rotation, Operation<BlockState> original, ServerLevelAccessor level) {
        instance = BlockReplacement.getBlockReplacement(level, instance);
        return original.call(instance, rotation);
    }
}
