package io.github.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CatLieOnBedGoal.class, priority = 1004)
public abstract class CatLieOnBedGoalMixin {
    @ModifyReturnValue(method = "isValidTarget", at = @At(value = "RETURN"))
    private boolean applyTagCatsOnBlocksLie(boolean original, LevelReader level, BlockPos pos) {
        return original || (level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(QuadBlockTags.CATS_ON_BLOCKS_LIE));
    }
}