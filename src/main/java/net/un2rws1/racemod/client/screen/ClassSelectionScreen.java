package net.un2rws1.racemod.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.networking.SelectClassPayload;

public class ClassSelectionScreen extends Screen {
    public ClassSelectionScreen() {
        super(Text.literal("Choose Your Race"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 220;
        int buttonHeight = 20;
        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;
        int spacing = 28;
        int i = 0;

        int index = 0;
        for (PlayerClass playerClass : PlayerClass.values()) {
            int x = centerX - (buttonWidth / 2);
            int y = startY + (index * spacing);
            int buttonY = y + (i * spacing);

            this.addDrawableChild(
                    ButtonWidget.builder(
                            Text.literal(playerClass.getDisplayName()),
                            button -> {
                                ClientPlayNetworking.send(new SelectClassPayload(playerClass.getId()));

                                if (this.client != null) {
                                    this.client.setScreen(null); // closes the GUI
                                }
                            }
                    ).dimensions(x, buttonY, buttonWidth, buttonHeight).build()
            );


            index++;
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int titleY = 30;

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                centerX,
                titleY,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Choose one carefully. This choice is permanent. You will stay Black"),
                centerX,
                titleY + 16,
                0xAAAAAA
        );

        int descStartY = this.height / 2 - 38;
        int spacing = 28;
        int index = 0;

        for (PlayerClass playerClass : PlayerClass.values()) {
            int descY = descStartY + (index * spacing) + 22;

            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    Text.literal(playerClass.getDescription()),
                    centerX,
                    descY,
                    0xCCCCCC
            );

            index++;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        // Prevent manual closing if the player has not chosen yet.
        // Leave empty to block ESC / close behavior.
    }
}