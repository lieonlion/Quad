package dev.lieonlion.quad.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.lieonlion.quad.Quad;
import dev.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PortalForcer.class, priority = 1004)
public abstract class PortalForcerMixin {
    @Unique
    protected final RandomSource quad$random = RandomSource.create();

    @WrapOperation(method = "createPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 2))
    private BlockState applyTagNetherPortalFormed(Block instance, Operation<BlockState> original) {
        HolderSet.Named<Block> tag_blocks = BuiltInRegistries.BLOCK.getOrThrow(QuadBlockTags.NETHER_PORTAL_FORMED);
        String tag_name = QuadBlockTags.NETHER_PORTAL_FORMED.location().toString();
        if (tag_blocks.size() == 1) {
            Block block = tag_blocks.get(0).value();
            Quad.LOG.info("[Quad] tag: '{}' has the 1 block of: '{}'", tag_name, block);
            return block.defaultBlockState();
        } else if (tag_blocks.size() > 1) {
            Quad.LOG.info("[Quad] nether portal picking from multiple blocks found in tag: '{}'", tag_name);
            return tag_blocks.getRandomElement(this.quad$random).get().value().defaultBlockState();
        }
        Quad.LOG.info("[Quad] tag: '{}' is empty or has experienced an error", tag_name);
        return original.call(instance);
    }
}