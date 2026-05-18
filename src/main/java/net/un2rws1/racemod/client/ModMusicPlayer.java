package net.un2rws1.racemod.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.un2rws1.racemod.sound.ModSounds;

import java.util.Random;

public class ModMusicPlayer {

    private static final Random RANDOM = new Random();
    private static int musicTimer = 0;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                return;
            }

            if (musicTimer > 0) {
                musicTimer--;
                return;
            }

            if (client.getSoundManager().isPlaying(
                    PositionedSoundInstance.music(ModSounds.BJE)
            )) {
                return;
            }

            if (RANDOM.nextInt(6000) == 0) {

                client.getSoundManager().play(
                        PositionedSoundInstance.music(ModSounds.BJE)
                );
                musicTimer = 12000;
            }
        });
    }
}