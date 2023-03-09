package com.flylx.wand_mod.render;

import com.flylx.wand_mod.client.BasicMagicModel;
import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.event.SwitchMagic;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BasicMagicRenderer extends GeoProjectilesRenderer<BasicMagic> {


    public BasicMagicRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BasicMagicModel());
    }

    @Override
    protected int getBlockLight(BasicMagic entity, BlockPos pos) {
        return 15;
    }

    @Override
    public RenderLayer getRenderType(BasicMagic animatable, float partialTick, MatrixStack poseStack,
                                     @Nullable VertexConsumerProvider bufferSource,
                                     @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {


        if(animatable.degree>0&&animatable.degree<60){
            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
                    animatable.getParticleX(0.5)+Math.sin((animatable.world.getTime()+partialTick)/8),
                    animatable.getY()+Math.cos((animatable.world.getTime()+partialTick)/8),
                    animatable.getParticleZ(0.5),
                    animatable.getVelocity().x,animatable.getVelocity().y,animatable.getVelocity().z);
        }


        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }

}
