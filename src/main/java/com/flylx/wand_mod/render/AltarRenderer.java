package com.flylx.wand_mod.render;

import com.flylx.wand_mod.entity.AltarEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public class AltarRenderer implements BlockEntityRenderer<AltarEntity> {



    public AltarRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(AltarEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {
        if (entity.isEmpty()) {
            return;
        }


        matrices.push();

        matrices.translate(0.5f, 0.55f, 0.55f);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(9));
        matrices.scale(0.6f, 0.6f, 0.6f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getContent(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);
        matrices.pop();

    }
}