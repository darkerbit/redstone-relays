package io.github.darkerbit.redstonerelays.client.render;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.entity.PulseRelayBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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
                .uv(0, 0).cuboid(0, 18, 0, 4, 4, 4),
                ModelTransform.NONE);

        return TexturedModelData.of(modelData, 64, 64);
    }

    private final ModelPart turnkeyModel;

    public PulseRelayBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx.getTextRenderer());

        turnkeyModel = ctx.getLayerModelPart(TURNKEY_LAYER).getChild("turnkey");
    }

    @Override
    public void render(PulseRelayBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);

        turnkeyModel.yaw = (float) Math.toRadians(entity.getAnimator().getRotation(tickDelta));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TURNKEY_TEXTURE));
        turnkeyModel.render(matrices, vertexConsumer, light, overlay);
    }
}
