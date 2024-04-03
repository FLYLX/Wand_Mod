package com.flylx.wand_mod.model;

import com.flylx.wand_mod.mob.MagicGolemEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class MagicGolemEntityModel extends IronGolemEntityModel<MagicGolemEntity> {

    public MagicGolemEntityModel(ModelPart root) {
        super(root);
    }

    @Override
    public ModelPart getPart() {
        return super.getPart();
    }

    @Override
    public void setAngles(MagicGolemEntity ironGolemEntity, float f, float g, float h, float i, float j) {
        super.setAngles(ironGolemEntity, f, g, h, i, j);
    }

    @Override
    public void animateModel(MagicGolemEntity ironGolemEntity, float f, float g, float h) {
        super.animateModel(ironGolemEntity, f, g, h);
    }

    @Override
    public ModelPart getRightArm() {
        return super.getRightArm();
    }
}
