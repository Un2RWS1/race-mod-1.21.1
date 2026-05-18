package net.un2rws1.racemod.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.un2rws1.racemod.sound.ModSounds;

import java.util.Random;

public class ModMusicPlayer {

    private static final Random RANDOM = new Random();

    private static final SoundEvent[] MUSIC_TRACKS = {
            ModSounds.BJE,
            ModSounds.JOALT,
            ModSounds.WTBMWG,
            ModSounds.D,
            ModSounds.OLA,
            ModSounds.SFTB,
            ModSounds.TFEO,
            ModSounds.TTFA,
    };

    private static PositionedSoundInstance currentSound = null;
    private static int musicTimer = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean inGame = client.player != null && client.world != null;
            if (currentSound != null &&
                    client.getSoundManager().isPlaying(currentSound)) {
                return;
            }

            if (musicTimer > 0) {
                musicTimer--;
                return;
            }

            if (RANDOM.nextInt(50) == 0) {

                SoundEvent track = MUSIC_TRACKS[
                        RANDOM.nextInt(MUSIC_TRACKS.length)
                        ];
                currentSound = PositionedSoundInstance.music(track);
                client.getSoundManager().play(currentSound);
                musicTimer = 1000;
            }
        });
    }
}