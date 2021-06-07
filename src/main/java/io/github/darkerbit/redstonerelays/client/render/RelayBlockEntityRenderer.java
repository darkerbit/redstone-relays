package io.github.darkerbit.redstonerelays.client.render;

import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import io.github.darkerbit.redstonerelays.client.RedstoneRelaysClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

public class RelayBlockEntityRenderer<T extends AbstractRelayBlockEntity> implements BlockEntityRenderer<T> {
    private static final float FACTOR = -1.0f/48.0f;
    private static final double VERTICAL = 6.0d/16.0d;

    private static final float PANEL_WIDTH = 1.0f / -FACTOR;
    private static final float PANEL_HEIGHT = (float) VERTICAL / -FACTOR;

    private final TextRenderer textRenderer;

    protected RelayBlockEntityRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    private void translate(MatrixStack matrices, int facing) {
        switch (facing) {
            case 0 -> matrices.translate(1.0d, VERTICAL, -0.01d);
            case 1 -> {
                matrices.translate(-0.01d, VERTICAL, 0.0d);
                matrices.multiply(new Quaternion(0, 90, 0, true));
            }
            case 2 -> {
                matrices.translate(0.0d, VERTICAL, 1.01d);
                matrices.multiply(new Quaternion(0, 180, 0, true));
            }
            case 3 -> {
                matrices.translate(1.01d, VERTICAL, 1.0d);
                matrices.multiply(new Quaternion(0, 270, 0, true));
            }
        }

        matrices.scale(FACTOR, FACTOR, FACTOR);
    }

    private void drawTextCentered(MatrixStack matrices, TextRenderer textRenderer, String text) {
        float x = PANEL_WIDTH / 2.0f - (float) textRenderer.getWidth(text) / 2.0f + 0.5f;
        float y = PANEL_HEIGHT / 2.0f - 4.0f;

        textRenderer.draw(matrices, text, x, y, 0xFFFFFF);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        String controlName = textRenderer.trimToWidth(RedstoneRelaysClient.getKeybindName(entity.getNumber()), (int) PANEL_WIDTH).getString();

        for (int i = 0; i < 4; i++) {
            matrices.push();
            translate(matrices, i);

            drawTextCentered(matrices, textRenderer, controlName);

            matrices.pop();
        }
    }

    public static <T extends AbstractRelayBlockEntity> BlockEntityRenderer<T> create(BlockEntityRendererFactory.Context ctx) {
        return new RelayBlockEntityRenderer<T>(ctx.getTextRenderer());
    }
}
