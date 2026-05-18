package net.un2rws1.racemod.mixin;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.util.Green_Card_Helper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "eatFood", at = @At("TAIL"))
    private void racemod$bonusFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        PlayerClass playerClass = ClassManager.getPlayerClass(serverPlayer);
        if (playerClass != PlayerClass.MEXICAN) {
            return;
        }
        HungerManager hungerManager = player.getHungerManager();
        int oldFood = hungerManager.getFoodLevel();
        int bonusFood = 3;
        int newFood = Math.min(20, oldFood + bonusFood);
        hungerManager.setFoodLevel(newFood);

    }
    @Inject(
            method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void racemod$deleteDroppedGreenCard(ItemStack stack,
                                                boolean throwRandomly,
                                                boolean retainOwnership,
                                                CallbackInfoReturnable<ItemEntity> cir) {

        PlayerEntity player = (PlayerEntity) (Object) this;

        if (Green_Card_Helper.isMEXICAN(player)
                && stack.isOf(ModItems.GREEN_CARD)) {
            stack.setCount(0);

            player.sendMessage(
                    Text.literal("Perdiste tus papeles."),
                    true
            );

            player.getWorld().playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
                    SoundCategory.PLAYERS,
                    10.0f,
                    1.0f
            );
            cir.setReturnValue(null);
        }
    }
}

