package io.github.lieonlion.quad.mixin;

import io.github.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PortalShape.class, priority = 1004)
public abstract class PortalShapeMixin {
    @Shadow private static @Final @Mutable BlockBehaviour.StatePredicate FRAME;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void PortalShape(CallbackInfo ci) {
        FRAME = (BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) ->
                blockState.is(QuadBlockTags.NETHER_PORTAL_BUILT);
    }
}