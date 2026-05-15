package net.un2rws1.racemod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.classsystem.ClassManager;

public class Green_Card_Helper {
    private static final java.util.Set<java.util.UUID> HAD_OFFHAND_TICKET =
            new java.util.HashSet<>();

    public static boolean isMEXICAN(PlayerEntity player) {
        return ClassManager.getPlayerClass(player) == PlayerClass.MEXICAN;
    }

    public static boolean hasGreen_Card(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(ModItems.GREEN_CARD)) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (stack.isOf(ModItems.GREEN_CARD)) {
                return true;
            }
        }

        for (ItemStack stack : player.getInventory().offHand) {
            if (stack.isOf(ModItems.GREEN_CARD)) {
                return true;
            }
        }

        return false;
    }

    public static boolean mexicanNeedsTicket(PlayerEntity player) {
        return isMEXICAN(player) && !hasTicketInOffhand(player);
    }
    public static boolean isNetherOrEnd(RegistryKey<World> worldKey) {
        return worldKey == World.NETHER || worldKey == World.END;
    }
    public static boolean hasTicketInOffhand(PlayerEntity player) {
        return player.getOffHandStack().isOf(ModItems.GREEN_CARD);
    }

    public static void removeTicketsNotInOffhand(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack stack = player.getInventory().main.get(i);

            if (stack.isOf(ModItems.GREEN_CARD)) {
                player.getInventory().main.set(i, ItemStack.EMPTY);
            }
        }

        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack stack = player.getInventory().armor.get(i);

            if (stack.isOf(ModItems.GREEN_CARD)) {
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
        }
    }

    public static void enforceMexicanTicketOffhand(PlayerEntity player) {
        if (!isMEXICAN(player)) {
            return;
        }
        boolean hasTicketNow = hasTicketInOffhand(player);
        boolean hadTicketBefore = HAD_OFFHAND_TICKET.contains(player.getUuid());

        if (hadTicketBefore && !hasTicketNow) {
            player.sendMessage(
                    Text.literal("Keep your papers in your off hand"),
                    true);
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(
                        new TitleS2CPacket(Text.literal("Perdiste tus papeles."))
                );
            }



            player.getWorld().playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
                    SoundCategory.PLAYERS,
                    10.0f,
                    1.0f
            );
        }

        if (hasTicketNow) {
            HAD_OFFHAND_TICKET.add(player.getUuid());
        } else {
            HAD_OFFHAND_TICKET.remove(player.getUuid());
        }
        removeTicketsNotInOffhand(player);
    }


    public static void sendNoTicketMessage(PlayerEntity player, String action) {
        if (!player.getWorld().isClient) {
            player.sendMessage(Text.literal("Necesitas tu papel " + action + "."), true);
        }
    }
}