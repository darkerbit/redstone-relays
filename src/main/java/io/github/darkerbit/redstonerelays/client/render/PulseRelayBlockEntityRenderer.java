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
import net.minecraft.util.math.Vec3f;

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
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-entity.getRenderOrientation()));
        matrices.translate(-0.5, -0.5, -0.5);

        turnkeyModel.yaw = (float) Math.toRadians(entity.getAnimator().getRotation(tickDelta));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TURNKEY_TEXTURE));
        turnkeyModel.render(matrices, vertexConsumer, light, overlay);

        matrices.pop();
    }
}
