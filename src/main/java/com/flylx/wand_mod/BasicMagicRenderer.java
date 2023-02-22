package com.flylx.wand_mod;

import com.flylx.wand_mod.client.BasicMagicModel;
import com.flylx.wand_mod.entity.BasicMagic;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BasicMagicRenderer extends GeoProjectilesRenderer<BasicMagic>
{

    public BasicMagicRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicMagicModel());
    }

    @Override
    protected int getBlockLight(BasicMagic entity, BlockPos pos) {
        return 15;
    }

    @Override
    public RenderLayer getRenderType(BasicMagic animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }
}
