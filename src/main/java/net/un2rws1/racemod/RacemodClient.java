package net.un2rws1.racemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.client.ClientClassState;
import net.un2rws1.racemod.client.screen.ClassSelectionScreen;
import net.un2rws1.racemod.entity.ModEntities;
import net.un2rws1.racemod.event.PlayerJoinHandler;
import net.un2rws1.racemod.networking.ModNetworking;
import net.un2rws1.racemod.networking.OpenClassSelectionPayload;
import net.un2rws1.racemod.networking.SyncClassPayload;


public class RacemodClient implements ClientModInitializer{
    @Override
    public void onInitializeClient() {
            EntityRendererRegistry.register(ModEntities.POOP, FlyingItemEntityRenderer::new);
            ClassAttachmentTypes.init();
            ModNetworking.register();
            PlayerJoinHandler.register();



        ClientPlayNetworking.registerGlobalReceiver(OpenClassSelectionPayload.ID, (payload, context) -> {
            System.out.println("[RaceMod] Received OpenClassSelectionPayload");
            context.client().execute(() -> {
                System.out.println("[RaceMod] Opening ClassSelectionScreen");
                context.client().setScreen(new ClassSelectionScreen());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncClassPayload.ID, (payload, context) -> {

            System.out.println("[RaceMod] RECEIVED: " + payload.classId());

            context.client().execute(() -> {
                PlayerClass parsed = PlayerClass.fromId(payload.classId());
                System.out.println("[RaceMod] PARSED: " + parsed);

                ClientClassState.setPlayerClass(parsed);
            });
        });
        System.out.println("[RaceMod] Client initializer loaded");
    }

}