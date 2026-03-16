package net.un2rws1.racemod.event;

import net.un2rws1.racemod.classsystem.ClassManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public final class PlayerJoinHandler {
    private PlayerJoinHandler() {
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            server.execute(() -> {
                System.out.println("[RaceMod] Player joined: " + handler.player.getName().getString());

                ClassManager.refreshCurrentClassEffects(handler.player);

                if (!ClassManager.hasClass(handler.player)) {
                    System.out.println("[RaceMod] Player has no class, opening GUI");
                    ClassManager.openSelection(handler.player);
                } else {
                    System.out.println("[RaceMod] Player already has a class");
                }
            });
        });
    }
}