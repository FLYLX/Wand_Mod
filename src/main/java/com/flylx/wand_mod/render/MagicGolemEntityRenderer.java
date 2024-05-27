package com.flylx.wand_mod.render;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.mob.MagicGolemEntity;
import com.flylx.wand_mod.mob.MagicGolemTypes;
import com.flylx.wand_mod.model.MagicGolemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class MagicGolemEntityRenderer extends MobEntityRenderer<MagicGolemEntity, MagicGolemEntityModel> {
    private static final Identifier FIRE = new Identifier(Wand_mod.ModID,"textures/mob/fire_golem.png");
    private static final Identifier FROZE = new Identifier(Wand_mod.ModID,"textures/mob/froze_golem.png");
    private static final Identifier POISON = new Identifier(Wand_mod.ModID,"textures/mob/poison_golem.png");
    private static final Identifier END = new Identifier(Wand_mod.ModID,"textures/mob/end_golem.png");
    private static final Identifier STONE = new Identifier(Wand_mod.ModID,"textures/mob/stone_golem.png");

    public MagicGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MagicGolemEntityModel(context.getPart(EntityModelLayers.IRON_GOLEM)), 0.7f);

    }

    @Override
    public Identifier getTexture(MagicGolemEntity entity) {
        if(entity.getMagicGolemTypes() == MagicGolemTypes.FIRE) {
            return FIRE;
        }else if(entity.getMagicGolemTypes() == MagicGolemTypes.FROZE){
            return FROZE;
        }else if(entity.getMagicGolemTypes() == MagicGolemTypes.POISON){
            return POISON;
        }else if(entity.getMagicGolemTypes() == MagicGolemTypes.END){
            return END;
        }else if(entity.getMagicGolemTypes() == MagicGolemTypes.STONE){
            return STONE;
        }else{
            return FIRE;
        }
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
