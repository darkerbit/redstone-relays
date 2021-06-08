package io.github.darkerbit.redstonerelays.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.client.RedstoneRelaysClient;
import io.github.darkerbit.redstonerelays.gui.RelayScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RelayScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = RedstoneRelays.identifier("textures/gui/container/relay.png");

    private RelayScreenHandler handler;

    private static final int BUTTON_WIDTH = 34;
    private static final int BUTTON_HEIGHT = 22;

    private RelayButtonWidget[] buttonWidgets = new RelayButtonWidget[10];

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

        buttonWidgets[button] = addDrawableChild(new RelayButtonWidget(
                this.x + startX + x * BUTTON_WIDTH + 1 + (y < 0 ? (int) (0.5 * BUTTON_WIDTH) : 0),
                this.y + startY + (2 - y) * BUTTON_HEIGHT + 1,
                (y < 0 ? 2 : 1) * BUTTON_WIDTH - 2, BUTTON_HEIGHT - 2,
                button, (y < 0 ? 2 : 1) * BUTTON_WIDTH - 4, textRenderer,
                buttonWidget -> {
                    client.interactionManager.clickButton(handler.syncId, button);
                }
        ));
    }

    private void setPressed(int num) {
        buttonWidgets[pressed].active = true;

        buttonWidgets[num].active = false;
        buttonWidgets[num].unFocus();
        setFocused(null);

        this.pressed = num;
    }

    private void setupButtons() {
        makeButton(0, 0, -1);

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int button_i = j * 3 + i + 1;

                makeButton(button_i, i, j);
            }
        }

        buttonWidgets[pressed].active = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int curPressed = handler.getRelayNumber();

        if (pressed != curPressed)
            setPressed(curPressed);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        this.drawTexture(matrices, x, y - 10, 0, 0, this.backgroundWidth, this.backgroundHeight + 10);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.draw(matrices, textRenderer.trimToWidth(title, backgroundWidth - 2 * titleX).getString(), titleX, titleY - 9, 4210752);
    }

    @Override
    protected void addElementNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.USAGE, RedstoneRelays.translateNow("gui", "relay_screen_narrator",
                handler.getRelayNumber(), RedstoneRelaysClient.getKeybindName(handler.getRelayNumber()).getString()));

        super.addElementNarrations(builder);
    }
}
