package net.un2rws1.racemod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;

import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float racemod_modifyDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity)(Object)this;
        //blacks cant damage golems
        if (entity instanceof IronGolemEntity) {
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                PlayerClass playerClass = ClassManager.getPlayerClass(player);

                if (playerClass == PlayerClass.BLACK) {
                    return 0.0f;
                }
            }
        }

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

        return amount;
    }
}