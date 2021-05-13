package io.github.darkerbit.redstonerelays.client.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
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

        setupButtons();
    }

    private void setupButtons() {
        int startX = backgroundWidth / 2 - 3 * BUTTON_WIDTH / 2;
        int startY = backgroundHeight / 2 - 4 * BUTTON_HEIGHT / 2;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int button_i = j * 3 + i + 1;

                buttonWidgets[button_i] = new ButtonWidget(
                        startX + i * BUTTON_WIDTH + 1,
                        startY + (2 - j) * BUTTON_HEIGHT + 1,
                        BUTTON_WIDTH - 2, BUTTON_HEIGHT - 2,
                        new LiteralText(Integer.toString(button_i)),
                        button -> {
                            client.interactionManager.clickButton(handler.syncId, button_i);
                        }
                );

                if (button_i == pressed) {
                    buttonWidgets[button_i].active = false;
                }
            }
        }

        buttonWidgets[0] = new ButtonWidget(
                startX + BUTTON_WIDTH / 2 + 1, startY + 3 * BUTTON_HEIGHT + 1,
                BUTTON_WIDTH * 2 - 2, BUTTON_HEIGHT - 2,
                new LiteralText("0"),
                button -> {
                    client.interactionManager.clickButton(handler.syncId, 0);
                }
        );

        if (pressed == 0)
            buttonWidgets[0].active = false;
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
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.client.getTextureManager().bindTexture(TEXTURE);

        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int newPressed = handler.getRelayNumber();

        if (pressed != newPressed) {
            pressed = newPressed;
            setupButtons();
        }

        textRenderer.draw(matrices, title, titleX, titleY, 4210752);

        for (ButtonWidget button : buttonWidgets) {
            button.render(matrices, mouseX - x, mouseY - y, 0.0f);
        }
    }
}
