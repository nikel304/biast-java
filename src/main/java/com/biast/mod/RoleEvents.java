package com.biast.mod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;\nimport net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BiastMod.MODID)
public class RoleEvents {
    @SubscribeEvent public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getTags().contains("biast_entity") && !player.getTags().contains("biast_contractee")) {
            player.addTag("biast_contractee");
        }
    }

    @SubscribeEvent public static void playerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.player.level().isClientSide || !e.player.getTags().contains("biast_entity")) return;
        Player attacker = e.player; long now = attacker.level().getGameTime();
        if (now < attacker.getPersistentData().getLong("biast_next_attack")) return;
        for (Player target : attacker.level().players()) {
            if (target == attacker || !target.getTags().contains("biast_contractee") || attacker.distanceToSqr(target) > 7.84) continue;
            float damage = switch (attacker.getPersistentData().getString("biast_entity_type")) { case "ashle" -> 10; case "observer" -> 6; case "cubedhexa22" -> 5; default -> 8; };
            target.hurt(attacker.damageSources().playerAttack((ServerPlayer) attacker), damage);
            attacker.getPersistentData().putLong("biast_next_attack", now + 20); break;
        }
    }
}
