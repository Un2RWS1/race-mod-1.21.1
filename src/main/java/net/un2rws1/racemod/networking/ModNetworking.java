package net.un2rws1.racemod.networking;

import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ModNetworking {
    private static boolean initialized = false;

    private ModNetworking() {
    }

    public static void register() {
        if (initialized) {
            return;
        }
        initialized = true;

        PayloadTypeRegistry.playS2C().register(OpenClassSelectionPayload.ID, OpenClassSelectionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SelectClassPayload.ID, SelectClassPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SelectClassPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                PlayerClass.byId(payload.classId()).ifPresent(playerClass ->
                        ClassManager.trySelectClass(context.player(), playerClass)
                );
            });
        });
    }
}