package com.flylx.wand_mod.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

public class MagicShieldParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected MagicShieldParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                                  SpriteProvider spriteSet) {
        super(level,xCoord,yCoord,zCoord);

        this.velocityMultiplier = 1f;
        this.spriteProvider = spriteSet;
        this.scale *= 0.5;
        this.setSpriteForAge(spriteSet);
        this.maxAge = 10;
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public void tick() {

       super.tick();
       this.setSpriteForAge(spriteProvider);

    }



    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factary implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider sprites;

        public Factary(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }


        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {

            return new MagicShieldParticle(world,x,y,z,this.sprites);
        }
    }
}
