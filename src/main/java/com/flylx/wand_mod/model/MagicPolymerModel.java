package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.mob.MagicPolymer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagicPolymerModel extends AnimatedGeoModel<MagicPolymer> {

    @Override
    public Identifier getModelResource(MagicPolymer object) {
        return new Identifier(Wand_mod.ModID,"geo/magic_polymer.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicPolymer object) {
        return new Identifier(Wand_mod.ModID,"textures/mob/magic_polymer.png");
    }

    @Override
    public Identifier getAnimationResource(MagicPolymer animatable) {
        return new Identifier(Wand_mod.ModID,"animations/magic_polymer.animation.json");
    }
}
