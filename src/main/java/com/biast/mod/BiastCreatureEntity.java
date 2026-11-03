package com.biast.mod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class BiastCreatureEntity extends Monster {
    public BiastCreatureEntity(EntityType<? extends Monster> type, Level level) { super(type, level); this.xpReward = 15; }
    public static AttributeSupplier.Builder observerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 55).add(Attributes.MOVEMENT_SPEED, .25).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.FOLLOW_RANGE, 36).add(Attributes.KNOCKBACK_RESISTANCE, .25);
    }
    public static AttributeSupplier.Builder cubedAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 45).add(Attributes.MOVEMENT_SPEED, .36).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.FOLLOW_RANGE, 32);
    }
    public static AttributeSupplier.Builder ashleAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.MOVEMENT_SPEED, .22).add(Attributes.ATTACK_DAMAGE, 11).add(Attributes.FOLLOW_RANGE, 30).add(Attributes.KNOCKBACK_RESISTANCE, .6);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15, false));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, .8));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 16));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, p -> !p.getTags().contains("biast_entity")));
    }
    @Override public void tick() {
        super.tick();
        if (level().isClientSide || !(getTarget() instanceof Player target) || !target.isAlive()) return;
        long now = level().getGameTime();
        String kind = getType() == BiastMod.OBSERVER.get() ? "observer" :
                getType() == BiastMod.CUBED_HEXA22.get() ? "cubedhexa22" : "ashle";
        long next = getPersistentData().getLong("biast_ability_next");
        if (now < next) return;
        double distance = distanceToSqr(target);
        switch (kind) {
            case "observer" -> {
                if (distance < 24 * 24) {
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                    if (distance > 5 * 5) addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 50, 0));
                }
                getPersistentData().putLong("biast_ability_next", now + 240);
            }
            case "cubedhexa22" -> {
                if (distance > 2.5 * 2.5 && distance < 13 * 13) {
                    Vec3 dir = target.position().subtract(position()).normalize();
                    setDeltaMovement(dir.x * 1.45, 0.30, dir.z * 1.45);
                    hasImpulse = true;
                }
                getPersistentData().putLong("biast_ability_next", now + 100);
            }
            case "ashle" -> {
                if (distance < 8 * 8) {
                    for (Player nearby : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(7),
                            pl -> !pl.getTags().contains("biast_entity"))) {
                        nearby.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                        nearby.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                    }
                }
                getPersistentData().putLong("biast_ability_next", now + 220);
            }
        }
    }

    @Override public boolean removeWhenFarAway(double distanceToClosestPlayer) { return false; }
}
