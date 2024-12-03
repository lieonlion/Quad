package io.github.lieonlion.quad.util;

import io.github.lieonlion.quad.tags.QuadItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class QuadUtil {
    public static void usedFireLighter(Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
        } usedFireLighter(level, pos, player, hand, stack);
    }

    public static void usedFireLighter(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        SoundEvent soundEvent = stack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        if (!stack.is(Items.ENCHANTED_BOOK) && !player.isCreative()) {
            if (stack.isDamageableItem()) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
                stack.shrink(1);
            }
        }
    }

    public static boolean hasEquipmentFromTag(TagKey<Item> itemTagKey, LivingEntity living) {
        for (ItemStack stack : living.getArmorSlots()) {
            if (stack.is(itemTagKey)) {
                return true;
            }
        } return false;
    }

    public static boolean isFireLighter(ItemStack stack) {
        return stack.is(QuadItemTags.FIRE_LIGHTER);
    }

    public static boolean isSnowActsSolid(ItemStack stack) {
        return stack.is(QuadItemTags.SNOW_ACTS_SOLID);
    }

    public static boolean hasBurnsProtector(LivingEntity living) {
        return hasEquipmentFromTag(QuadItemTags.PROTECTS_FROM_BURNS, living);
    }

    public static boolean hasPiglinPacifier(LivingEntity living) {
        return hasEquipmentFromTag(QuadItemTags.PACIFIER_PIGLIN, living);
    }

    public static boolean hasEndermanPacifier(LivingEntity living) {
        return hasEquipmentFromTag(QuadItemTags.PACIFIER_ENDERMAN, living);
    }

    public static boolean hasSnowBoots(LivingEntity living) {
        return hasEquipmentFromTag(QuadItemTags.SNOW_BOOTS, living);
    }
}