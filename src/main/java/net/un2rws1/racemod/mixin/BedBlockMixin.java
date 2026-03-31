package net.un2rws1.racemod.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.un2rws1.racemod.util.Green_Card_Helper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void racemod$blockMexicanBedUse(BlockState state, World world, BlockPos pos,
                                            PlayerEntity player, BlockHitResult hit,
                                            CallbackInfoReturnable<ActionResult> cir) {
        if (Green_Card_Helper.mexicanNeedsTicket(player)) {
            Green_Card_Helper.sendNoTicketMessage(player, "use beds");
            player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

}