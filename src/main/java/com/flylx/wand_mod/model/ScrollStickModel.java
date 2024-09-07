package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.ScrollStick;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ScrollStickModel  extends AnimatedGeoModel<ScrollStick> {
    @Override
    public Identifier getModelResource(ScrollStick object) {
        return new Identifier(Wand_mod.ModID,"geo/scroll_stick.geo.json");
    }

    @Override
    public Identifier getTextureResource(ScrollStick object) {
        return new Identifier(Wand_mod.ModID,"textures/item/empty_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(ScrollStick animatable) {
        return null;
    }
}
