package io.github.darkerbit.redstonerelays.client.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.client.RedstoneRelaysClient;
import io.github.darkerbit.redstonerelays.gui.RelayScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RelayScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = RedstoneRelays.identifier("textures/gui/container/relay.png");

    private RelayScreenHandler handler;

    private static final int BUTTON_WIDTH = 34;
    private static final int BUTTON_HEIGHT = 22;

    private ButtonWidget[] buttonWidgets = new ButtonWidget[10];

    private int pressed = 0;

    public RelayScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.handler = (RelayScreenHandler) handler;
    }

    @Override
    protected void init() {
        this.backgroundWidth = 114;
        this.backgroundHeight = 104;

        super.init();
        setupButtons();
    }

    private void makeButton(int button, int x, int y) {
        int startX = backgroundWidth / 2 - 3 * BUTTON_WIDTH / 2;
        int startY = backgroundHeight / 2 - 4 * BUTTON_HEIGHT / 2;

        buttonWidgets[button] = new ButtonWidget(
                startX + x * BUTTON_WIDTH + 1 + (y < 0 ? (int) (0.5 * BUTTON_WIDTH) : 0),
                startY + (2 - y) * BUTTON_HEIGHT + 1,
                (y < 0 ? 2 : 1) * BUTTON_WIDTH - 2, BUTTON_HEIGHT - 2,
                new LiteralText(textRenderer.trimToWidth(RedstoneRelaysClient.getKeybindName(button), BUTTON_WIDTH - 4).getString()),
                buttonWidget -> {
                    client.interactionManager.clickButton(handler.syncId, button);
                }
        );

        if (button == pressed) {
            buttonWidgets[button].active = false;
        }
    }

    private void setupButtons() {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int button_i = j * 3 + i + 1;

                makeButton(button_i, i, j);
            }
        }

        makeButton(0, 0, -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        for (ButtonWidget button : buttonWidgets) {
             if (button.mouseClicked(mouseX - x, mouseY - y, buttonId))
                 return true;
        }

        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.client.getTextureManager().bindTexture(TEXTURE);

        this.drawTexture(matrices, x, y - 10, 0, 0, this.backgroundWidth, this.backgroundHeight + 10);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int newPressed = handler.getRelayNumber();

        if (pressed != newPressed) {
            pressed = newPressed;
            setupButtons();
        }

        textRenderer.draw(matrices, textRenderer.trimToWidth(title, backgroundWidth - 2 * titleX).getString(), titleX, titleY - 10, 4210752);

        for (ButtonWidget button : buttonWidgets) {
            button.render(matrices, mouseX - x, mouseY - y, 0.0f);
        }
    }
}
