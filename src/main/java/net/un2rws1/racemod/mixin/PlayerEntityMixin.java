package net.un2rws1.racemod.mixin;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

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
}
