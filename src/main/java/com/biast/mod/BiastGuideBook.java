package com.biast.mod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** In-game quick reference for BIAST commands and gameplay. */
public class BiastGuideBook extends Item {
    public BiastGuideBook(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            player.sendSystemMessage(Component.literal("===== BIAST: СПРАВОЧНИК =====").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("/biast help — список команд").withStyle(ChatFormatting.YELLOW));
            player.sendSystemMessage(Component.literal("/biast list — список сущностей и ролей"));
            player.sendSystemMessage(Component.literal("/biast spawn <slacker|observer|cubedhexa22|ashle> — призвать NPC"));
            player.sendSystemMessage(Component.literal("/biast entity <игрок> <тип> — назначить игрока Entity"));
            player.sendSystemMessage(Component.literal("/biast human <игрок> — вернуть роль Contractee"));
            player.sendSystemMessage(Component.literal("/biast dash — рывок Contractee"));
            player.sendSystemMessage(Component.literal("Роли: Contractees собирают артефакты; Entity охотятся на них."));
            player.sendSystemMessage(Component.literal("LMS начинается, когда в раунде остаётся один Contractee.").withStyle(ChatFormatting.LIGHT_PURPLE));
            player.sendSystemMessage(Component.literal("Музыка LMS: ALL BETS ARE OFF — длительность 1:38.406."));
            player.sendSystemMessage(Component.literal("Предметы: аптечка, оглушающий талисман, заряд рывка, пустотный артефакт."));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
