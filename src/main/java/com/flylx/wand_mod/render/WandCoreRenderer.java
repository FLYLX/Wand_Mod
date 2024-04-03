package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.WandCore;

import com.flylx.wand_mod.model.WandCoreModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class WandCoreRenderer extends GeoItemRenderer<WandCore> {
    public WandCoreRenderer() {
            super(new WandCoreModel());
    }



    @Override
    public void render(WandCore animatable, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, ItemStack stack) {
        super.render(animatable, poseStack, bufferSource, packedLight, stack);

    }
}
