package com.flylx.wand_mod.render;

import com.flylx.wand_mod.entity.MagicAreaCloud;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MagicCloudRenderer extends EntityRenderer<MagicAreaCloud> {
    public MagicCloudRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);

    }

    @Override
    public Identifier getTexture(MagicAreaCloud entity) {
        return null;
    }

    @Override
    public void render(MagicAreaCloud entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
