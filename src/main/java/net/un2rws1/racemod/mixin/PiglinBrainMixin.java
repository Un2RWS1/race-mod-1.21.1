package net.un2rws1.racemod.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.un2rws1.racemod.access.PiglinTraderAccess;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Inject(method = "getBarteredItem", at = @At("RETURN"), cancellable = true)
    private static void racemod$bonusBarterForMages(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (piglin.getWorld().isClient()) {
            return;
        }

        UUID traderUuid = ((PiglinTraderAccess)piglin).racemod$getLastTrader();
        if (traderUuid == null) {
            return;
        }

        ServerPlayerEntity player = piglin.getWorld().getServer().getPlayerManager().getPlayer(traderUuid);
        if (player == null) {
            return;
        }

        if (ClassManager.getPlayerClass(player) != PlayerClass.JEW) {
            return;
        }

        List<ItemStack> modified = new ArrayList<>(cir.getReturnValue());
        if (piglin.getRandom().nextFloat() < 0.5f) {
            modified.add(new ItemStack(Items.ENDER_PEARL, 1));
        }
        if (piglin.getRandom().nextFloat() < 0.3f) {
            modified.add(new ItemStack(Items.EMERALD, 1));
        }
        if (piglin.getRandom().nextFloat() < 0.3f) {
            modified.add(new ItemStack(Items.BLAZE_POWDER, 3));
        }
        if (piglin.getRandom().nextFloat() < 0.05f) {
            modified.add(new ItemStack(Items.GOLD_BLOCK, 1));
        }
        if (piglin.getRandom().nextFloat() < 0.01f) {
            modified.add(new ItemStack(Items.DIAMOND, 6));
        }
        if (piglin.getRandom().nextFloat() < 0.001f) {
            modified.add(new ItemStack(Items.DIAMOND_BLOCK, 3));
        }
        if (piglin.getRandom().nextFloat() < 0.0005f) {
            modified.add(new ItemStack(Items.NETHERITE_BLOCK, 1));
        }

        cir.setReturnValue(modified);

        //clear after use so old player data doesn't linger
        ((PiglinTraderAccess)piglin).racemod$setLastTrader(null);
    }
}