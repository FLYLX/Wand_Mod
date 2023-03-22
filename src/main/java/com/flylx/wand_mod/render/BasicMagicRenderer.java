package com.flylx.wand_mod.render;

import com.flylx.wand_mod.model.BasicMagicModel;
import com.flylx.wand_mod.entity.BasicMagic;
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

        if(animatable.degree>=0&&animatable.degree<60){
            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, true,animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),animatable.getVelocity().x,animatable.getVelocity().y ,animatable.getVelocity().z );
//            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
//                    animatable.getParticleX(0.5)+Math.cos((animatable.world.getTime()+partialTick)/8),
//                    animatable.getY()+Math.sin((animatable.world.getTime()+partialTick)/8),
//                    animatable.getZ(),
//                    animatable.getVelocity().x,animatable.getVelocity().y,animatable.getVelocity().z);
            if (!animatable.isExist){
                for(int q = 0;q<5;q++) {
                    for (int i = 0; i < 360; i = i + 10) {

                        animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, animatable.getParticleX(0.0D) + Math.sin(i)*q,
                                animatable.getY(),
                                animatable.getParticleZ(0.0D) + Math.cos(i)*q, Math.sin(i),
                                0, Math.cos(i));

                    }
                }
                animatable.setVelocity(0,0,0);

            }

            }else if(animatable.degree>=60&&animatable.degree<120){
            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, true,animatable.getParticleX(0.5D),
                    animatable.getY(),
                    animatable.getParticleZ(0.5D),
                    animatable.getVelocity().x,animatable.getVelocity().y ,animatable.getVelocity().z );
            if (!animatable.isExist) {
                for (int q = 0; q < 5; q++) {
                    for (int i = 0; i < 360; i = i + 10) {
                        animatable.world.addParticle(ParticleTypes.DRIPPING_WATER, animatable.getParticleX(0.0D) + Math.sin(i)*q,
                                animatable.getY(),
                                animatable.getParticleZ(0.0D) + Math.cos(i)*q, Math.sin(i),
                                0, Math.cos(i));
                    }
                }
                animatable.setVelocity(0,0,0);

            }

            }
//            animatable.world.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER,
//                    animatable.getParticleX(0.5)+Math.cos((animatable.world.getTime()+partialTick)/8),
//                    animatable.getY()+Math.sin((animatable.world.getTime()+partialTick)/8),
//                    animatable.getParticleZ(0.5),
//                    animatable.getVelocity().x,animatable.getVelocity().y,animatable.getVelocity().z);
//        }

        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }



}
