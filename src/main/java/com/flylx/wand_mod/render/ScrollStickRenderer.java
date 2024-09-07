package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.ScrollStick;
import com.flylx.wand_mod.model.ScrollStickModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ScrollStickRenderer extends GeoItemRenderer<ScrollStick> {
    public ScrollStickRenderer() {
        super(new ScrollStickModel());
    }

}
