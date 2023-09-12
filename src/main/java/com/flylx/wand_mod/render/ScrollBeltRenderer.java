package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.ScrollBeltItem;
import com.flylx.wand_mod.model.ScrollBeltModel;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ScrollBeltRenderer extends GeoArmorRenderer<ScrollBeltItem> {
    public ScrollBeltRenderer() {
        super(new ScrollBeltModel());
        this.bodyBone = "armorBody";
    }
}
