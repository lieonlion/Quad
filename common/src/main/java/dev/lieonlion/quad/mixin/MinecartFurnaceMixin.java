package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MinecartFurnace.class, priority = 1004)
public abstract class MinecartFurnaceMixin extends AbstractMinecart {
    @Shadow private int fuel;
    @Shadow public Vec3 push;

    public MinecartFurnaceMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "addFuel", at = @At(value = "RETURN"))
    private boolean applyAbstractFurnaceFuelItems(boolean original, Vec3 playerPosition, ItemStack stack) {
        if (stack.isEmpty()) return false;
        int itemFuelTime = level().fuelValues().burnDuration(stack);
        if (itemFuelTime > 0 && this.fuel + itemFuelTime <= 32000) {
            this.fuel += itemFuelTime;

            if (this.fuel > 0) {
              this.push = this.position().subtract(playerPosition).horizontal();
            }
            return true;
        } return false;
    }
}
