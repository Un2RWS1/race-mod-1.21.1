package net.un2rws1.racemod.mixin.client;

import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void stopVanillaMusic(MusicSound type, CallbackInfo ci) {
        ci.cancel();
    }
}