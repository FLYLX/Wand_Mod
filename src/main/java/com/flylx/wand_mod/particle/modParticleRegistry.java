package com.flylx.wand_mod.particle;

import com.flylx.wand_mod.Wand_mod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class modParticleRegistry {
    public static final DefaultParticleType MAGICSHIELD_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles(){
        Registry.register(Registry.PARTICLE_TYPE,new Identifier(Wand_mod.ModID,"magic_shield"),
                MAGICSHIELD_PARTICLE);
    }
}
