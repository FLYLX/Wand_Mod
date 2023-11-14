package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.MagicShield;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagicShieldModel  extends AnimatedGeoModel<MagicShield> {
    @Override
    public Identifier getModelResource(MagicShield object) {
        return new Identifier(Wand_mod.ModID,"geo/magic_shield.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagicShield object) {
        return new Identifier(Wand_mod.ModID,"textures/item/magic_shield.png");
    }

    @Override
    public Identifier getAnimationResource(MagicShield animatable) {
        return new Identifier(Wand_mod.ModID,"animations/magic_shield.animation.json");
    }
}
