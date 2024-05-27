package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.WandBox;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WandBoxModel extends AnimatedGeoModel<WandBox> {
    @Override
    public Identifier getModelResource(WandBox object) {
        return new Identifier(Wand_mod.ModID,"geo/wand_box.geo.json");
    }

    @Override
    public Identifier getTextureResource(WandBox object) {
        return new Identifier(Wand_mod.ModID,"textures/item/wand_box.png");
    }

    @Override
    public Identifier getAnimationResource(WandBox animatable) {
        return new Identifier(Wand_mod.ModID,"animations/wand_box.animation.json");
    }
}
