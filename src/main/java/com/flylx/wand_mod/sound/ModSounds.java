package com.flylx.wand_mod.sound;

import com.flylx.wand_mod.Wand_mod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final SoundEvent FIRE_MAGIC = registerSoundEvent("fire");
    public static final SoundEvent FROZE_MAGIC = registerSoundEvent("froze");
    public static final SoundEvent POISON_MAGIC = registerSoundEvent("poison");
    public static final SoundEvent CURE_MAGIC = registerSoundEvent("cure");
    public static final SoundEvent WAND_SWING = registerSoundEvent("swing");
    public static final SoundEvent MAGIC_HIT = registerSoundEvent("hit");
    public static final SoundEvent SPAWN_ITEM = registerSoundEvent("spawn");
    public static final SoundEvent SPAWN_END = registerSoundEvent("spawn_end");

    public static final SoundEvent MAGIC_POLYMER_AMBIENT = registerSoundEvent("magic_polymer_ambient");
    public static final SoundEvent MAGIC_POLYMER_HURT = registerSoundEvent("magic_polymer_hurt");

    public static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(Wand_mod.ModID,name);
        return Registry.register(Registry.SOUND_EVENT,id,new SoundEvent(id));
    }
}
