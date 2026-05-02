package net.un2rws1.racemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.client.ClientClassState;
import net.un2rws1.racemod.client.screen.ClassSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.un2rws1.racemod.entity.ModEntities;
import net.un2rws1.racemod.event.PlayerJoinHandler;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.mixin.client.GameRendererAccessor;
import net.un2rws1.racemod.network.MuslimRitualPayload;
import net.un2rws1.racemod.networking.ModNetworking;
import net.un2rws1.racemod.networking.OpenClassSelectionPayload;
import net.un2rws1.racemod.networking.StealAttemptPayload;
import net.un2rws1.racemod.networking.SyncClassPayload;
import org.lwjgl.glfw.GLFW;


public class RacemodClient implements ClientModInitializer{
    private static KeyBinding stealKey;
    private static boolean blurEnabled = false;
    public static KeyBinding MUSLIM_RITUAL_KEY;


    @Override
    public void onInitializeClient() {
            EntityRendererRegistry.register(ModEntities.POOP, FlyingItemEntityRenderer::new);
            ClassAttachmentTypes.init();
            ModNetworking.register();
            PlayerJoinHandler.register();
        stealKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.racemod.steal",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.racemod"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (stealKey.wasPressed()) {
                if (client.player == null || client.world == null) return;
                int targetId = -1;
                Entity hit = client.targetedEntity;
                if (hit instanceof PlayerEntity target) {
                    targetId = target.getId();
                }
                ClientPlayNetworking.send(new StealAttemptPayload(targetId));
            }
            while (MUSLIM_RITUAL_KEY.wasPressed()) {
                ClientPlayNetworking.send(new MuslimRitualPayload(true));
            }


            // blurr
            if (client.player == null || client.world == null) return;

            boolean shouldBlur = shouldChineseBeBlurred(client);

            if (shouldBlur != blurEnabled) {
                blurEnabled = shouldBlur;
                updateBlurShader(client, blurEnabled);
            }
                    while (MUSLIM_RITUAL_KEY.wasPressed()) {
                        ClientPlayNetworking.send(new MuslimRitualPayload(true));
                    }
            });



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
        MUSLIM_RITUAL_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.racemod.bomber_ritual",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_P,
                        "category.racemod"
                )
        );

        System.out.println("[RaceMod] Client initializer loaded");

    }
    // blur
    private static boolean shouldChineseBeBlurred(MinecraftClient client) {
         boolean isChinese = ClientClassState.getPlayerClass() == PlayerClass.CHINESE;
        ItemStack headStack = client.player.getEquippedStack(EquipmentSlot.HEAD);
        boolean wearingGlasses = headStack.isOf(ModItems.GLASSES);
        return isChinese && !wearingGlasses;
    }
    private static void updateBlurShader(MinecraftClient client, boolean enable) {
        if (client.gameRenderer == null) return;

        if (enable) {
            ((GameRendererAccessor) client.gameRenderer)
                    .racemod$loadPostProcessor(
                            Identifier.of(Racemod.MOD_ID, "shaders/post/blurry_vision.json")
                    );
        } else {
            client.gameRenderer.disablePostProcessor();
        }
    }

}