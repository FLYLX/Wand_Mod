package com.flylx.wand_mod.render;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.mob.MagicGolemEntity;
import com.flylx.wand_mod.model.MagicGolemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.IronGolemCrackFeatureRenderer;
import net.minecraft.client.render.entity.feature.IronGolemFlowerFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.apache.logging.log4j.LogManager;

import java.util.Random;

public class MagicGolemEntityRenderer extends MobEntityRenderer<MagicGolemEntity, MagicGolemEntityModel> {
    private static final Identifier TEXTURE = new Identifier(Wand_mod.ModID,"textures/mob/magic_golem.png");


    public MagicGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MagicGolemEntityModel(context.getPart(EntityModelLayers.IRON_GOLEM)), 0.7f);

    }

    @Override
    public Identifier getTexture(MagicGolemEntity entity) {
        return TEXTURE;
    }



    @Override
    protected void setupTransforms(MagicGolemEntity magicGolemEntity , MatrixStack matrixStack, float f, float g, float h) {
        Random random = new Random();
        super.setupTransforms(magicGolemEntity, matrixStack, f, g, h);
        if ((double)magicGolemEntity.limbDistance < 0.01) {
            return;
        }
        float i = 13.0f;
        float j = magicGolemEntity.limbAngle - magicGolemEntity.limbDistance * (1.0f - h) + 6.0f;
        float k = (Math.abs(j % 13.0f - 6.5f) - 3.25f) / 3.25f;
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(6.5f * k));


    }


}
