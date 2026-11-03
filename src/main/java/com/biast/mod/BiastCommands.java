package com.biast.mod;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BiastMod.MODID)
public class BiastCommands {
    @SubscribeEvent public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("biast")
            .then(Commands.literal("help").executes(c -> { c.getSource().sendSuccess(() -> Component.literal("BIAST: /biast help, /biast list, /biast spawn <type>, /biast entity <player> <type>, /biast human <player>, /biast dash. Give guide: /give @s biast:command_book. LMS starts with one Contractee left. Types: slacker, observer, cubedhexa22, ashle."), false); return 1; }))
            .then(Commands.literal("list").executes(c -> { c.getSource().sendSuccess(() -> Component.literal("Entities: slacker, observer, cubedhexa22, ashle. Roles: Entity and Contractee."), false); return 1; }))
            .then(Commands.literal("spawn").then(Commands.argument("type", StringArgumentType.word()).suggests((c,b) -> { b.suggest("slacker"); b.suggest("observer"); b.suggest("cubedhexa22"); b.suggest("ashle"); return b.buildFuture(); }).executes(c -> {
                String type = StringArgumentType.getString(c, "type").toLowerCase(); Entity e = create(type, c.getSource().getLevel());
                if (e == null) { c.getSource().sendFailure(Component.literal("Unknown entity. Use /biast list")); return 0; }
                Vec3 p = c.getSource().getPosition(); e.moveTo(p.x, p.y, p.z, c.getSource().getRotation().y, 0); c.getSource().getLevel().addFreshEntity(e);
                c.getSource().sendSuccess(() -> Component.literal("Spawned " + type), true); return 1;
            })))
            .then(Commands.literal("entity").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("type", StringArgumentType.word()).executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player"); String type = StringArgumentType.getString(c, "type").toLowerCase();
                if (!valid(type)) { c.getSource().sendFailure(Component.literal("Unknown entity. Use /biast list")); return 0; }
                p.removeTag("biast_contractee"); p.addTag("biast_entity"); p.getPersistentData().putString("biast_entity_type", type);
                p.setCustomName(Component.literal(type)); p.setCustomNameVisible(true);
                c.getSource().sendSuccess(() -> Component.literal(p.getGameProfile().getName() + " is now " + type), true); return 1;
            }))))
            .then(Commands.literal("dash").executes(c -> {
                if (!(c.getSource().getEntity() instanceof ServerPlayer player)) { c.getSource().sendFailure(Component.literal("Только игрок может использовать рывок.")); return 0; }
                if (player.getTags().contains("biast_entity")) { c.getSource().sendFailure(Component.literal("Рывок доступен только Contractees.")); return 0; }
                long now = player.level().getGameTime();
                long ready = player.getPersistentData().getLong("biast_dash_ready");
                if (now < ready) { c.getSource().sendFailure(Component.literal("Рывок перезаряжается: " + ((ready-now+19)/20) + " сек.")); return 0; }
                Vec3 look = player.getLookAngle();
                Vec3 horizontal = new Vec3(look.x, 0, look.z).normalize();
                player.setDeltaMovement(horizontal.x * 1.55, Math.max(player.getDeltaMovement().y, 0.18), horizontal.z * 1.55);
                player.hasImpulse = true;
                player.getPersistentData().putLong("biast_dash_ready", now + 80);
                c.getSource().sendSuccess(() -> Component.literal("Рывок! Перезарядка: 4 секунды."), false); return 1;
            }))
            .then(Commands.literal("human").then(Commands.argument("player", EntityArgument.player()).executes(c -> {
                ServerPlayer p = EntityArgument.getPlayer(c, "player"); p.removeTag("biast_entity"); p.addTag("biast_contractee"); p.getPersistentData().remove("biast_entity_type");
                p.setCustomName(null); p.setCustomNameVisible(false); c.getSource().sendSuccess(() -> Component.literal(p.getGameProfile().getName() + " is now a Contractee"), true); return 1;
            })))
        );
    }
    private static boolean valid(String s) { return s.equals("slacker") || s.equals("observer") || s.equals("cubedhexa22") || s.equals("ashle"); }
    private static Entity create(String s, net.minecraft.server.level.ServerLevel l) { return switch (s) {
        case "slacker" -> BiastMod.SLACKER.get().create(l); case "observer" -> BiastMod.OBSERVER.get().create(l);
        case "cubedhexa22" -> BiastMod.CUBED_HEXA22.get().create(l); case "ashle" -> BiastMod.ASHLE.get().create(l); default -> null; }; }
}
