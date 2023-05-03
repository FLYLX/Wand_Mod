package com.flylx.wand_mod.render;

import com.flylx.wand_mod.model.BasicMagicModel;
import com.flylx.wand_mod.entity.BasicMagic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
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

        if(animatable.getDegree()>=0&&animatable.getDegree()<60&&!MinecraftClient.getInstance().isPaused()){
            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, true,animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),animatable.getVelocity().x,animatable.getVelocity().y ,animatable.getVelocity().z );


            }else if(animatable.getDegree()>=60&&animatable.getDegree()<120&&!MinecraftClient.getInstance().isPaused()){
            animatable.world.addParticle(ParticleTypes.SNOWFLAKE, true,animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),
                    0,0,0 );
            }else if(animatable.getDegree()>=120&&animatable.getDegree()<180&&!MinecraftClient.getInstance().isPaused()){
            int m = 0x00FF22;
            double n = (float) (m >> 16 & 0xFF) / 255.0f;
            double o = (float) (m >> 8 & 0xFF) / 255.0f;
            double p = (float) (m & 0xFF) / 255.0f;
            animatable.world.addParticle(ParticleTypes.ENTITY_EFFECT, true,animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),
                    n,o,p );
        }else if(animatable.getDegree()>=180&&animatable.getDegree()<270&&!MinecraftClient.getInstance().isPaused()){
            int m = 0xFF0000;
            double n = (float) (m >> 16 & 0xFF) / 255.0f;
            double o = (float) (m >> 8 & 0xFF) / 255.0f;
            double p = (float) (m & 0xFF) / 255.0f;
            Particle particle =
                    MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.INSTANT_EFFECT,
                    animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),
                    n,o,p );
            particle.setColor((float) n,(float)o,(float)p);

        }

        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }



}
