package net.un2rws1.racemod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.classsystem.ClassState;
import net.un2rws1.racemod.classsystem.PlayerClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.un2rws1.racemod.classsystem.ClassManager.getPlayerClass;
import static net.un2rws1.racemod.classsystem.ClassManager.getState;


@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin {

    @Inject(method = "quickMove", at = @At("TAIL"))
    private void racemod$doubleExplosivesShiftClick(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (player.getWorld().isClient()) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (slot != 0) return;

        ItemStack crafted = cir.getReturnValue();
        if (crafted == null || crafted.isEmpty()) return;

        ClassState state = getState(serverPlayer);
        if (state == null) return;
        if (getPlayerClass(player) != PlayerClass.MUSLIM) return;

        if (
                crafted.isOf(Items.TNT) ||
                crafted.isOf(Items.FIREWORK_ROCKET) ||
                crafted.isOf(Items.END_CRYSTAL)
        ) {
            ItemStack bonus = crafted.copy();
            serverPlayer.getInventory().offerOrDrop(bonus);
        }
    }
}