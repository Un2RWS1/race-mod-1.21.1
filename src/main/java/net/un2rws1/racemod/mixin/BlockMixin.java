package net.un2rws1.racemod.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class BlockMixin {

    @Inject(
            method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContextParameterSet$Builder;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void racemod$extraMageCropDrops(
            BlockState state,
            LootContextParameterSet.Builder builder,
            CallbackInfoReturnable<List<ItemStack>> cir
    ) {
        Block self = (Block) (Object) this;
        if (!(self instanceof CropBlock crop)) return;
        if (!crop.isMature(state)) return;
        Entity entity = builder.getOptional(LootContextParameters.THIS_ENTITY);
        if (!(entity instanceof ServerPlayerEntity player)) return;

        PlayerClass playerClass = ClassManager.getPlayerClass(player);
        if (playerClass == PlayerClass.CHINESE) {
            List<ItemStack> drops = new ArrayList<>(cir.getReturnValue());
            for (ItemStack stack : drops) {
                stack.increment(2);
            }
            cir.setReturnValue(drops);
        }
    }
}