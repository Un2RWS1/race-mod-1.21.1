package net.un2rws1.racemod.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.util.Green_Card_Helper;
import net.un2rws1.racemod.villager.ModVillagerProfessions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void racemod$blockMexicanVillagerTrade(PlayerEntity player, Hand hand,
                                                   CallbackInfoReturnable<ActionResult> cir) {
        if (Green_Card_Helper.mexicanNeedsTicket(player)) {
            Green_Card_Helper.sendNoTicketMessage(player, "trade with villagers");
            player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

}