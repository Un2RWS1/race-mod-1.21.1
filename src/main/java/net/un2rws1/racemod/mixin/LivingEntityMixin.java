package net.un2rws1.racemod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.text.Text;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}