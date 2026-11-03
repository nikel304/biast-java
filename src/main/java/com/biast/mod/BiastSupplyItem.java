package com.biast.mod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BiastSupplyItem extends Item {
    public enum Kind { MEDKIT, STUN_CHARM, DASH_CHARGE }
    private final Kind kind;
    public BiastSupplyItem(Kind kind) { super(new Item.Properties().stacksTo(4)); this.kind = kind; }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);
        if (!level.isClientSide) {
            switch (kind) {
                case MEDKIT -> player.heal(8.0F);
                case STUN_CHARM -> {
                    for (Player target : level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(5.0D),
                            p -> p != player && p.getTags().contains("biast_entity"))) {
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1));
                    }
                }
                case DASH_CHARGE -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
            }
            if (!player.getAbilities().instabuild) stack.shrink(1);
            player.getCooldowns().addCooldown(this, 100);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
