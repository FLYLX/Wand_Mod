package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.animated_base_wand;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BaseWandModel extends AnimatedGeoModel<animated_base_wand> {
    @Override
    public Identifier getModelResource(animated_base_wand object) {
        return new Identifier(Wand_mod.ModID,"geo/base_wand.geo.json");
    }

    @Override
    public Identifier getTextureResource(animated_base_wand object) {
        return new Identifier(Wand_mod.ModID,"textures/item/base_wand.png");
    }

    @Override
    public Identifier getAnimationResource(animated_base_wand animatable) {
        return new Identifier(Wand_mod.ModID,"animations/animated_item_animation.json");
    }
}
