package net.un2rws1.racemod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.classsystem.ClassState;
import net.un2rws1.racemod.classsystem.PlayerClass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.un2rws1.racemod.classsystem.ClassManager.getPlayerClass;
import static net.un2rws1.racemod.classsystem.ClassManager.getState;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {

    @Shadow @Final private PlayerEntity player;

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void racemod$doubleExplosives(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (player.getWorld().isClient()) return;

        ClassState state = getState(serverPlayer);
        if (state == null) return;
        if (getPlayerClass(player) != PlayerClass.MUSLIM) return;

        if (
                stack.isOf(Items.TNT) ||
                stack.isOf(Items.FIREWORK_ROCKET) ||
                stack.isOf(Items.END_CRYSTAL)
        ) {
            ItemStack bonus = stack.copy();
            serverPlayer.getInventory().offerOrDrop(bonus);
        }
    }
}