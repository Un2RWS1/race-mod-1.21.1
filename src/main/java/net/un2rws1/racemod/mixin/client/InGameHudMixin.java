package net.un2rws1.racemod.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.client.ClientClassState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Unique
    private static final Identifier COINSLOT_VISION =
            Identifier.of(Racemod.MOD_ID, "textures/gui/chinese_overlay.png");

    @Inject(method = "renderMiscOverlays", at = @At("TAIL"))
    private void racemod$renderCoinslotVision(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (client.player == null) return;
        if (ClientClassState.getPlayerClass() != PlayerClass.CHINESE) return;
        racemod$renderOverlay(context, COINSLOT_VISION, 1.0F);
    }

    @Unique
    private void racemod$renderOverlay(DrawContext context, Identifier texture, float opacity) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        context.drawTexture(
                texture,
                0, 0,
                -90,
                0.0F, 0.0F,
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight()
        );
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}