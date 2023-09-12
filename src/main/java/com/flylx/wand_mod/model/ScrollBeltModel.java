package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.ScrollBeltItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ScrollBeltModel extends AnimatedGeoModel<ScrollBeltItem> {

    @Override
    public Identifier getModelResource(ScrollBeltItem object) {
        return new Identifier(Wand_mod.ModID,"geo/scroll_belt.geo.json");
    }

    @Override
    public Identifier getTextureResource(ScrollBeltItem object) {
        return new Identifier(Wand_mod.ModID,"textures/item/empty_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(ScrollBeltItem animatable) {
        return null;
    }
}
