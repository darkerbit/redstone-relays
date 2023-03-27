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

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.entity.PulseRelayBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.AxisAngle4d;
import org.joml.Math;
import org.joml.Quaternionf;

public class PulseRelayBlockEntityRenderer extends RelayBlockEntityRenderer<PulseRelayBlockEntity> {
    private static final Identifier TURNKEY_TEXTURE = RedstoneRelays.identifier("textures/entity/pulse_relay_turnkey.png");

    private static EntityModelLayer TURNKEY_LAYER = new EntityModelLayer(RedstoneRelays.identifier("turnkey"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(TURNKEY_LAYER, PulseRelayBlockEntityRenderer::model);
    }

    private static TexturedModelData model() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();

        partData.addChild("turnkey", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-1, 0, -0.5f, 2, 2, 1)
                .uv(0, 4).cuboid(-2, 2, -0.5f, 4, 1, 1)
                .uv(8, 0).cuboid(-1, 3, -0.5f, 2, 1, 1),
                ModelTransform.pivot(8, 6, 5.5f));

        return TexturedModelData.of(modelData, 16, 8);
    }

    private final ModelPart turnkeyModel;

    public PulseRelayBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx.getTextRenderer());

        turnkeyModel = ctx.getLayerModelPart(TURNKEY_LAYER).getChild("turnkey");
    }

    @Override
    public void render(PulseRelayBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);

        matrices.push();

        // rotate to match blockstate
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(new Quaternionf(new AxisAngle4d(Math.toRadians(-entity.getRenderOrientation()), 0, 1, 0)));
        matrices.translate(-0.5, -0.5, -0.5);

        turnkeyModel.yaw = (float) Math.toRadians(entity.getAnimator().getRotation(tickDelta));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TURNKEY_TEXTURE));
        turnkeyModel.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
    }
}
