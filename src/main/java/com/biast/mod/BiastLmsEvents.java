package com.biast.mod;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Starts the supplied LMS theme once a started round reaches its final Contractee. */
@Mod.EventBusSubscriber(modid = BiastMod.MODID)
public class BiastLmsEvents {
    private static final int LMS_DURATION_TICKS = 1968; // 98.406 seconds at 20 TPS
    private static final Map<MinecraftServer, Integer> PREVIOUS_CONTRACTEES = new WeakHashMap<>();
    private static final Map<MinecraftServer, Long> LMS_END_TICK = new WeakHashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = event.getServer();
        int aliveContractees = 0;
        ServerPlayer last = null;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive() && player.getTags().contains("biast_contractee")) {
                aliveContractees++;
                last = player;
            }
        }
        int previous = PREVIOUS_CONTRACTEES.getOrDefault(server, 0);
        long now = server.getTickCount();

        if (aliveContractees >= 2) {
            PREVIOUS_CONTRACTEES.put(server, aliveContractees);
        } else if (aliveContractees == 1 && previous >= 2 && !LMS_END_TICK.containsKey(server)) {
            LMS_END_TICK.put(server, now + LMS_DURATION_TICKS);
            ServerPlayer finalPlayer = last;
            finalPlayer.serverLevel().playSound(null, finalPlayer.blockPosition(), BiastMod.LMS_THEME.get(),
                    SoundSource.RECORDS, 4.0F, 1.0F);
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("LAST MAN STANDING! ALL BETS ARE OFF!").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD), false);
        } else if (aliveContractees == 0) {
            PREVIOUS_CONTRACTEES.put(server, 0);
        }

        Long end = LMS_END_TICK.get(server);
        if (end != null && now >= end) {
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("LMS завершён: время песни истекло. Раунд окончен!").withStyle(ChatFormatting.GOLD), false);
            LMS_END_TICK.remove(server);
            PREVIOUS_CONTRACTEES.put(server, 0);
        }
    }
}
