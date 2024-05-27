package com.flylx.wand_mod.render;

import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.model.BasicMagicModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BasicMagicRenderer extends GeoProjectilesRenderer<BasicMagic> {

    public static final Identifier CRYSTAL_BEAM_TEXTURE = new Identifier("textures/entity/end_crystal/end_crystal_beam.png");
    private static final RenderLayer CRYSTAL_BEAM_LAYER = RenderLayer.getEntitySmoothCutout(CRYSTAL_BEAM_TEXTURE);

    private static final Identifier TEXTURE = new Identifier("textures/item/ender_eye.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
    private static final double field_33632 = 960.0;
    private double last_x=0,last_y=0,last_z=0;
    private static final float SINE_45_DEGREES = (float)Math.sin(0.7853981633974483);


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


        return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
    }


    @Override
    public void render(BasicMagic animatable, float yaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);

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
        }else if(animatable.getDegree()>=180&&animatable.getDegree()<240&&!MinecraftClient.getInstance().isPaused()){
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

        }else if(animatable.getDegree()>=240&&animatable.getDegree()<300&&!MinecraftClient.getInstance().isPaused()){

                hook_render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);
                //鱼钩那样的特效
        }else if(animatable.getDegree()>=300&&animatable.getDegree()<360&&!MinecraftClient.getInstance().isPaused()){
            int m = 0x533D3A;
            double n = (float) (m >> 16 & 0xFF) / 255.0f;
            double o = (float) (m >> 8 & 0xFF) / 255.0f;
            double p = (float) (m & 0xFF) / 255.0f;
            Particle particle =
                    MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.ASH,
                            animatable.getParticleX(0.5D),
                            animatable.getY(),
                            animatable.getParticleZ(0.5D),
                            n,o,p );
            particle.setColor((float) n,(float)o,(float)p);


        }




    }


    public void hook_render(BasicMagic basicMagic, float f, float g, MatrixStack matrixStack,
                            VertexConsumerProvider vertexConsumerProvider, int i) {


        double s;
        float r;
        double q;
        double p;
        double o;

        LivingEntity playerEntity = (LivingEntity) basicMagic.getOwner();
        if (playerEntity == null) {
            return;
        }
        matrixStack.push();
        matrixStack.push();
        matrixStack.push();
        matrixStack.scale(0.3f, 0.3f, 0.3f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        int j = playerEntity.getMainArm() == Arm.RIGHT ? 1 : -1;
        ItemStack itemStack = playerEntity.getMainHandStack();
        if (!itemStack.isOf(Items.FISHING_ROD)) {
            j = -j;
        }
        float h = playerEntity.getHandSwingProgress(g);
        float k = MathHelper.sin(MathHelper.sqrt(h) * (float)Math.PI);
        float l = MathHelper.lerp(g, playerEntity.prevBodyYaw, playerEntity.bodyYaw) * ((float)Math.PI / 180);
        double d = MathHelper.sin(l);
        double e = MathHelper.cos(l);
        double m = (double)j * 0.35;
        double n = 0.8;
        if (this.dispatcher.gameOptions != null && !this.dispatcher.gameOptions.getPerspective().isFirstPerson() || playerEntity != MinecraftClient.getInstance().player) {
            o = MathHelper.lerp(g, playerEntity.prevX, playerEntity.getX()) - e * m - d * 0.8;
            p = playerEntity.prevY + (double)playerEntity.getStandingEyeHeight() + (playerEntity.getY() - playerEntity.prevY) * (double)g - 0.45;
            q = MathHelper.lerp(g, playerEntity.prevZ, playerEntity.getZ()) - d * m + e * 0.8;
            r = playerEntity.isInSneakingPose() ? -0.1875f : 0.0f;
        } else {
            s = 960.0 / (double)this.dispatcher.gameOptions.getFov().getValue().intValue();
            Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)j * 0.1f, -0.1f);
            vec3d = vec3d.multiply(s);
            vec3d = vec3d.rotateY(k * 0.5f);
            vec3d = vec3d.rotateX(-k * 0.7f);
            o = MathHelper.lerp(g, playerEntity.prevX, playerEntity.getX()) + vec3d.x;
            p = MathHelper.lerp(g, playerEntity.prevY, playerEntity.getY()) + vec3d.y;
            q = MathHelper.lerp(g, playerEntity.prevZ, playerEntity.getZ()) + vec3d.z;
            r = playerEntity.getStandingEyeHeight();
        }
        s = MathHelper.lerp(g, basicMagic.prevX, basicMagic.getX());
        double t = MathHelper.lerp(g, basicMagic.prevY, basicMagic.getY()) + 0.25;
        double u = MathHelper.lerp(g, basicMagic.prevZ, basicMagic.getZ());
        float v = (float)(o - s);
        float w = (float)(p - t) + r;
        float x = (float)(q - u);
        int y = 16;

        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry2 = matrixStack.peek();
//        matrixStack.scale(0.5f, 0.5f, 0.5f);
//        matrixStack.multiply(this.dispatcher.getRotation());
//        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        for (int z = 0; z <= 16; ++z) {
//            renderLine(v, w, x, vertexConsumer2, entry2, percentage(z, 16),
//                    percentage(z + 1, 16),basicMagic,vertexConsumerProvider,i,matrixStack);
        }
        matrixStack.pop();
        MatrixStack.Entry entry3 = matrixStack.peek();
        last_x = basicMagic.getX();
        last_y = basicMagic.getY();
        last_z = basicMagic.getZ();
        for (int z = 0; z <= 16; ++z) {
            rend_texture(v, w, x,  percentage(z, 16),
                    percentage(z + 1, 16),basicMagic,vertexConsumerProvider,i,matrixStack,g);

        }
        matrixStack.pop();
    }

    private static void vertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
        buffer.vertex(matrix, x - 0.5f, (float)y - 0.5f, 0.0f).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }



    private void renderLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices,
                            float segmentStart, float segmentEnd, BasicMagic basicMagic,
                            VertexConsumerProvider vertexConsumerProvider, int p, MatrixStack matrixStack) {

        float f = x * segmentStart;
        float g = y * ( 3*segmentStart - segmentStart*segmentStart) * 0.5f + 0.25f;
        float h = z * segmentStart;
        float i = x * segmentEnd - f;
        float j = y * ( 3*segmentEnd - segmentEnd*segmentEnd) * 0.5f + 0.25f - g;
        float k = z * segmentEnd - h;
        float l = MathHelper.sqrt(i * i + j * j + k * k);

//
//
////        vertexConsumer.vertex(matrices.getPositionMatrix(), f, g, h).color(255, 255, 255, 255).normal(matrices.getNormalMatrix(),
////                i /= l, j /= l, k /= l).texture(1,1).light(p).overlay(OverlayTexture.DEFAULT_UV).next();
//
//
//        matrixStack.translate(MathHelper.cos(f),MathHelper.cos(g),MathHelper.cos(h)-MathHelper.cos(g));
//
//        if(last_x!=0&&last_y!=0&&last_z!=0){
//            matrixStack.translate(-last_x,-last_y,-last_z);
//
//            matrixStack.translate(-f,g,-h-g);
//            last_x = f;
//            last_y = g;
//            last_z = -h-g;
//        }else{
//            matrixStack.translate(-f,g,-h-g);
//            last_x = f;
//            last_y = g;
//            last_z = -h-g;
//        }
//        matrixStack.pop();
//        matrices = matrixStack.peek();
//
//
//        basicMagic.getWorld().addParticle(modParticleRegistry.MAGICSHIELD_PARTICLE,basicMagic.getX()+f,
//                basicMagic.getY()+g,
//                basicMagic.getZ()+h,0,0,0);

        buffer.vertex(matrices.getPositionMatrix(), f, g, h).color(255, 255, 255, 255).normal(matrices.getNormalMatrix(),
                i /= l, j /= l, k /= l).next();

    }

    public static float getYOffset(BasicMagic basicMagic, float tickDelta) {
        float f = (float)basicMagic.basicmagicAge + tickDelta;
        float g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f;
        return g - 1.4f;
    }

    public void rend_texture(float x, float y, float z,
                             float segmentStart, float segmentEnd, BasicMagic basicMagic,
                             VertexConsumerProvider vertexConsumerProvider, int i1, MatrixStack matrixStack,
                             float g1){
        float h1 = getYOffset(basicMagic, g1);


        float f = x * segmentStart;
        float g = y * ( 3*segmentStart - segmentStart*segmentStart) * 0.5f ;
        float h = z * segmentStart;
        float i = x * segmentEnd - f;
        float j = y * ( 3*segmentEnd - segmentEnd*segmentEnd) * 0.5f  - g;
        float k = z * segmentEnd - h;
//        float l = MathHelper.sqrt(i * i + j * j + k * k);
//        matrixStack.scale(0.5f, 0.5f, 0.5f);
//        matrixStack.multiply(this.dispatcher.getRotation());
//        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
//        matrixStack.scale(0.5f, 0.5f, 0.5f);
//        matrixStack.multiply(this.dispatcher.getRotation());
//        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
//        matrices = matrixStack.peek();
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
//        测试用
        if (basicMagic.getOwner() != null) {
//            float m = (float)basicMagic.getOwner().getX()  ;
//            float n = (float)basicMagic.getOwner().getY() ;
//            float o = (float)basicMagic.getOwner().getZ() ;
//            float p = (float)((double)m - basicMagic.getX());
//            float q = (float)((double)n - basicMagic.getY());
//            float r = (float)((double)o - basicMagic.getZ());

            float m = (float)basicMagic.getX() + f  ;
            float n = (float)basicMagic.getY() + g ;
            float o = (float)basicMagic.getZ() + h;

            float p = (float)((double)m - last_x);
            float q = (float)((double)n - last_y);
            float r = (float)((double)o - last_z);

            last_x = basicMagic.getX()+f;
            last_y = basicMagic.getY()+g;
            last_z = basicMagic.getZ()+h;
            matrixStack.translate(p, q, r);
            renderCrystalBeam(-p, -q, -r, g1, basicMagic.basicmagicAge, matrixStack, vertexConsumerProvider, i1);

//            float l = (float)(basicMagic.getX() - MathHelper.lerp((double)g, basicMagic.getOwner().prevX, basicMagic.getOwner().getX()));
//            float m = (float)(basicMagic.getY() - MathHelper.lerp((double)g, basicMagic.getOwner().prevY, basicMagic.getOwner().getY()));
//            float r = (float)(basicMagic.getZ() - MathHelper.lerp((double)g, basicMagic.getOwner().prevZ, basicMagic.getOwner().getZ()));
//            renderCrystalBeam(l, m + getYOffset(basicMagic, g), r, g, basicMagic.basicmagicAge, matrixStack, vertexConsumerProvider,
//                    i1);

        }

//        if(last_x!=0&&last_y!=0&&last_z!=0){
//            matrixStack.translate(-last_x,-last_y,-last_z);
////
//            matrixStack.translate(f,g,h-g);
//            last_x = f;
//            last_y = g;
//            last_z = h-g;
//        }else{
//            matrixStack.translate(f,g,h-g);
//            last_x = f;
//            last_y = g;
//            last_z = h-g;
//        }

    }


    private static float percentage(int value, int max) {
        return (float)value / (float)max;
    }

    public static void renderCrystalBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float g = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float)(-Math.atan2(dz, dx)) - 1.5707964f));
        matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float)(-Math.atan2(f, dy)) - 1.5707964f));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER);
        float h = 0.0f - ((float)age + tickDelta) * 0.01f;
        float i = MathHelper.sqrt(dx * dx + dy * dy + dz * dz) / 32.0f - ((float)age + tickDelta) * 0.01f;
        int j = 8;
        float k = 0.0f;
        float l = 0.75f;
        float m = 0.0f;
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        for (int n = 1; n <= 8; ++n) {
            float o = MathHelper.sin((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float p = MathHelper.cos((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float q = (float)n / 8.0f;
//            RGB(187,97,235)
            vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(255, 255, 255, 0).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, k* 0.2f, l* 0.2f, g).color(255, 255, 255, 0).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o* 0.2f, p* 0.2f, g).color(255, 255, 255, 0).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(255, 255, 255,0).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next();
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }

}
