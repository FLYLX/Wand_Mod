package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.FlameScroll;

import com.flylx.wand_mod.model.FlameScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class FlameScrollRenderer extends GeoItemRenderer<FlameScroll> {
    public FlameScrollRenderer() {
        super(new FlameScrollModel());
    }

}
