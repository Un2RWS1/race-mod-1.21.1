package net.un2rws1.racemod.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.ClassState;

public final class PlayerRespawnHandler {
    private PlayerRespawnHandler() {
    }

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            ClassState oldState = oldPlayer.getAttachedOrCreate(ClassAttachmentTypes.PLAYER_CLASS);
            ClassState newState = newPlayer.getAttachedOrCreate(ClassAttachmentTypes.PLAYER_CLASS);

            if (oldState.hasChosenClass()) {
                newState.setSelectedClassId(oldState.getSelectedClassId());
            } else {
                newState.clear();
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ClassManager.refreshCurrentClassEffects(newPlayer);
        });
    }
}