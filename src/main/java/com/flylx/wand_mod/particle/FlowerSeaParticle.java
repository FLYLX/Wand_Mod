package com.flylx.wand_mod.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import org.jetbrains.annotations.Nullable;

public class FlowerSeaParticle  extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected FlowerSeaParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.gravityStrength = -0.015F;
        this.velocityMultiplier = 1.0F;
        this.spriteProvider = spriteProvider;
        this.velocityX = velocityX + (Math.random() * 2.0D - 1) * 0.05000000074505806D;
        this.velocityY = velocityY + (Math.random() * 2.0D + 1.0D) * 0.05000000074505806D;
        this.velocityZ = velocityZ + (Math.random() * 2.0D - 1) * 0.05000000074505806D;
        this.scale = 0.03F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.maxAge = (int)(16.0D / ((double)this.random.nextFloat() * 0.8D + 0.2D))+1;
        this.setSpriteForAge(spriteProvider);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
        this.velocityX *= 0.949999988079071D;
        this.velocityY *= 0.8999999761581421D;
        this.velocityZ *= 0.949999988079071D;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            FlowerSeaParticle flowerSeaParticle = new FlowerSeaParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            flowerSeaParticle.setColor(0.923F, 0.964F, 0.999F);
            return flowerSeaParticle;
        }
    }
}
