package net.un2rws1.racemod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.ClassState;
import net.un2rws1.racemod.classsystem.PlayerClass;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void racemod_blockClassDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(source.getAttacker() instanceof ServerPlayerEntity player)) {
            return;
        }

        PlayerClass playerClass = ClassManager.getPlayerClass(player);
        if (playerClass == null) {
            return;
        }

        // blacks cannot damage iron golems
        if (entity instanceof IronGolemEntity && playerClass == PlayerClass.BLACK) {
            player.sendMessage(Text.literal("You can't breath"), true);
            cir.setReturnValue(false);
            return;
        }

        // jews cannot damage pigs
        if (entity instanceof PigEntity && playerClass == PlayerClass.JEW) {
                player.sendMessage(Text.literal("You're a Jew"), true);
                cir.setReturnValue(false);
                return;
            }


        // Indians cannot damage cows
        if (entity instanceof CowEntity && playerClass == PlayerClass.INDIAN) {
                if (!player.getWorld().isClient()) {
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(player.getWorld());
                    if (lightning != null) {
                        lightning.refreshPositionAfterTeleport(
                                player.getX(),
                                player.getY(),
                                player.getZ()
                        );
                        player.getWorld().spawnEntity(lightning);
                    }
                    player.sendMessage(Text.literal("You're trying to kill your God"), true);
                }

                cir.setReturnValue(false);
                return;

        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float racemod_modifyDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (!(entity instanceof ServerPlayerEntity player)) {
            return amount;
        }

        PlayerClass playerClass = ClassManager.getPlayerClass(player);
        if (playerClass == null) {
            return amount;
        }

        Entity attacker = source.getAttacker();

        if (playerClass == PlayerClass.BLACK && source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                return amount * 1.5f; // 25% more projectile damage
            }
        if (playerClass == PlayerClass.BLACK && attacker instanceof IronGolemEntity) {
            amount = player.getMaxHealth() * 10.0f;
        }
        if (playerClass == PlayerClass.JEW) {
            return amount * 3f;
        }
        if (playerClass == PlayerClass.JEW && attacker instanceof LivingEntity living && living.getType().isIn(EntityTypeTags.RAIDERS)) {
            amount = player.getMaxHealth() * 10.0f;
        }
        if (playerClass == PlayerClass.JEW && attacker instanceof PillagerEntity) {
            amount = player.getMaxHealth() * 10.0f;
        }

        return amount;
    }
    @Inject(
            method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void racemod$blockAggro(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self instanceof MobEntity mob)) return;
        if (!(target instanceof PlayerEntity player)) return;

        PlayerClass playerClass = ClassManager.getPlayerClass(player);
        if (mob instanceof PiglinEntity && playerClass == PlayerClass.JEW) {
            cir.setReturnValue(false);
            return;
        }
        if (mob instanceof HoglinEntity && playerClass == PlayerClass.JEW) {
            cir.setReturnValue(false);
        }
        if (mob instanceof PiglinBruteEntity && playerClass == PlayerClass.JEW) {
            cir.setReturnValue(false);
        }
    }
 // =================================== extra meat ==========================
 @Inject(method = "dropLoot", at = @At("TAIL"))
        private void racemod$mageExtraDrops(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
     LivingEntity entity = (LivingEntity) (Object) this;
     if (!(entity.getWorld() instanceof ServerWorld world)) return;
     if (!(entity instanceof AnimalEntity)) return;
     if (!(source.getAttacker() instanceof ServerPlayerEntity player)) return;
     PlayerClass playerClass = ClassManager.getPlayerClass(player);
     if (playerClass == PlayerClass.CHINESE) {

         if (entity instanceof CowEntity) {
             entity.dropStack(new ItemStack(Items.BEEF, 1));
             entity.dropStack(new ItemStack(Items.LEATHER, 1));
         } else if (entity instanceof PigEntity) {
             entity.dropStack(new ItemStack(Items.PORKCHOP, 1));
         } else if (entity instanceof SheepEntity) {
             entity.dropStack(new ItemStack(Items.MUTTON, 1));
         } else if (entity instanceof ChickenEntity) {
             entity.dropStack(new ItemStack(Items.CHICKEN, 1));
             entity.dropStack(new ItemStack(Items.FEATHER, 1));
         }
     }
 }



}