package dev.lieonlion.quad.mixin;

import dev.lieonlion.quad.tags.QuadBlockTags;
import net.minecraft.world.entity.animal.golem.CopperGolemAi;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;

import java.util.function.Predicate;

@Mixin(value = CopperGolemAi.class, priority = 1004)
public class CopperGolemAiMixin {
    @Mutable @Shadow @Final private static Predicate<BlockState> TRANSPORT_ITEM_DESTINATION_BLOCK = (blockState) -> blockState.is(QuadBlockTags.COPPER_GOLEM_ITEM_DESTINATION);
}
