package com.flylx.wand_mod.render;

import com.flylx.wand_mod.entity.MagicAreaCloud;
import com.flylx.wand_mod.entity.MagicShieldEffect;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MagicShieldEffectRenderer extends EntityRenderer<MagicShieldEffect> {
    public MagicShieldEffectRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Override
    public Identifier getTexture(MagicShieldEffect entity) {
        return null;
    }

    @Override
    public void render(MagicShieldEffect entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
