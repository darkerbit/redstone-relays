/*
 * Copyright (c) 2023 darkerbit
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package io.github.darkerbit.redstonerelays.client.render;

import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import io.github.darkerbit.redstonerelays.client.RedstoneRelaysClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.AxisAngle4d;
import org.joml.Math;
import org.joml.Quaternionf;

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
                matrices.multiply(new Quaternionf(new AxisAngle4d(Math.toRadians(90), 0, 1, 0)));
            }
            case 2 -> {
                matrices.translate(0.0d, VERTICAL, 1.01d);
                matrices.multiply(new Quaternionf(new AxisAngle4d(Math.toRadians(180), 0, 1, 0)));
            }
            case 3 -> {
                matrices.translate(1.01d, VERTICAL, 1.0d);
                matrices.multiply(new Quaternionf(new AxisAngle4d(Math.toRadians(270), 0, 1, 0)));
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
