package net.un2rws1.racemod.mixin;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin {

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void racemod_addTargetGoal(CallbackInfo ci) {
        IronGolemEntity golem = (IronGolemEntity) (Object) this;

        ((MobEntityAccessor) golem).racemod$getTargetSelector().add(
                2,
                new ActiveTargetGoal<>(
                        golem,
                        PlayerEntity.class,
                        true,
                        target -> {
                            if (!(target instanceof ServerPlayerEntity serverPlayer)) {
                                return false;
                            }

                            if (serverPlayer.isCreative() || serverPlayer.isSpectator()) {
                                return false;
                            }

                            PlayerClass playerClass = ClassManager.getPlayerClass(serverPlayer);

                            return playerClass == PlayerClass.BLACK
                                    || playerClass == PlayerClass.MEXICAN;
                        }
                )
        );
    }
}