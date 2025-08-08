package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lieonlion.quad.util.QuadUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.MagmaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MagmaBlock.class, priority = 1004)
public class MagmaBlockMixin {
    @WrapOperation(method = "stepOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSteppingCarefully()Z"))
    private boolean applyTagBurnProtector(Entity instance, Operation<Boolean> original) {
        if (instance instanceof LivingEntity livingInstance) {
            return original.call(instance) || QuadUtil.hasBurnProtector(livingInstance);
        }
        return original.call(instance);
    }
}
