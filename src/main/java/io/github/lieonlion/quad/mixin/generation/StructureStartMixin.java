package io.github.lieonlion.quad.mixin.generation;

import io.github.lieonlion.quad.replacement.BlockReplacement;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StructureStart.class)
public abstract class StructureStartMixin {
    @Shadow
    @Final
    private Structure structure;

    @Inject(method = "placeInChunk", at = @At("HEAD"))
    private void pushStructure(CallbackInfo ci) {
        BlockReplacement.pushStructure(this.structure);
    }

    @Inject(method = "placeInChunk", at = @At("RETURN"))
    private void popStructure(CallbackInfo ci) {
        BlockReplacement.pushStructure(null);
    }
}
