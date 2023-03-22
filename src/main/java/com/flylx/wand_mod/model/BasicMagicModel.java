package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.entity.BasicMagic;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BasicMagicModel extends AnimatedGeoModel<BasicMagic> {
    @Override
    public Identifier getModelResource(BasicMagic object) {
        return new Identifier(Wand_mod.ModID,"geo/basic_magic.geo.json");
    }

    @Override
    public Identifier getTextureResource(BasicMagic object) {
            return new Identifier(Wand_mod.ModID, "textures/item/basic_magic.png");

    }

    @Override
    public Identifier getAnimationResource(BasicMagic animatable) {
        return new Identifier(Wand_mod.ModID,"animations/basic_magic_animation.json");
    }
}
