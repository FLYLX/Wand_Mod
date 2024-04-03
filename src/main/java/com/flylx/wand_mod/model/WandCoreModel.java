package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.WandCore;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WandCoreModel  extends AnimatedGeoModel<WandCore> {
    @Override
    public Identifier getModelResource(WandCore object) {
        return new Identifier(Wand_mod.ModID,"geo/wand_core.geo.json");
    }

    @Override
    public Identifier getTextureResource(WandCore object) {
        return  new Identifier(Wand_mod.ModID,"textures/item/wand_core.png");
    }

    @Override
    public Identifier getAnimationResource(WandCore animatable) {
        return new Identifier(Wand_mod.ModID,"animations/wand_core.animation.json");
    }
}
