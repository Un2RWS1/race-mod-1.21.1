package net.un2rws1.racemod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.classsystem.ClassManager;

public class Green_Card_Helper {

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
        return isMEXICAN(player) && !hasGreen_Card(player);
    }

    public static void sendNoTicketMessage(PlayerEntity player, String action) {
        if (!player.getWorld().isClient) {
            player.sendMessage(Text.literal("Necesitas tu papel " + action + "."), true);
        }
    }
}