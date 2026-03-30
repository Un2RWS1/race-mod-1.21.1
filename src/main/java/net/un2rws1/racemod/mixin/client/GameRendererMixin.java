package net.un2rws1.racemod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.effect.ModEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Unique
    private static final Identifier RACEMOD_BLUR =
            Identifier.of(Racemod.MOD_ID, "shaders/post/blurry_vision.json");

    @Unique
    private boolean racemod$loaded = false;

    @Inject(method = "render", at = @At("HEAD"))
    private void racemod$renderBlurryVision(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {

        if (client.player == null || client.world == null) {
            return;
        }

        boolean hasBlur = client.player.hasStatusEffect(ModEffects.BLURRY_VISION);

        if (hasBlur) {

            if (!racemod$loaded) {
                ((GameRendererAccessor)(Object)this)
                        .racemod$loadPostProcessor(RACEMOD_BLUR);
                racemod$loaded = true;
            }

            PostEffectProcessor processor = ((GameRenderer)(Object)this).getPostProcessor();
            if (processor != null) {
                processor.setUniforms("Radius", 10.0f);
            }

        } else if (racemod$loaded) {
            ((GameRenderer)(Object)this).disablePostProcessor();
            racemod$loaded = false;
        }
    }
}