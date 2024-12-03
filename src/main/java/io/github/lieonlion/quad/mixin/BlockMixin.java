package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.lieonlion.quad.tags.QuadBlockTags;
import io.github.lieonlion.quad.util.QuadUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Block.class, priority = 1004)
public abstract class BlockMixin {
    @Shadow public abstract BlockState defaultBlockState();

    @Inject(method = "stepOn", at = @At(value = "HEAD"))
    private void applyTagWhenSteppedOnBurns(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo ci) {
        if (!defaultBlockState().is(QuadBlockTags.WHEN_STEPPED_ON_BURNS)) return;
        if (entity instanceof LivingEntity living && !entity.isSteppingCarefully() &&
                !QuadUtil.hasBurnsProtector(living)) {
            entity.hurt(level.damageSources().hotFloor(), 1.0f);
        }
    }

    @ModifyReturnValue(method = "getSpeedFactor", at = @At(value = "RETURN"))
    private float applyTagWhenSteppedOnSlows(float original) {
        return defaultBlockState().is(QuadBlockTags.WHEN_STEPPED_ON_SLOWS) ? 0.4f : original;
    }

    @ModifyReturnValue(method = "getJumpFactor", at = @At(value = "RETURN"))
    private float applyTagsWhenSteppedOnSticks(float original) {
        return defaultBlockState().is(QuadBlockTags.WHEN_STEPPED_ON_STICKS) ? 0.5f : original;
    }
}