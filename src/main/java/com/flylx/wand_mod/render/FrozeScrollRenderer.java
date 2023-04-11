package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.FrozeScroll;

import com.flylx.wand_mod.model.FrozeScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class FrozeScrollRenderer extends GeoItemRenderer<FrozeScroll> {
    public FrozeScrollRenderer() {
        super(new FrozeScrollModel());
    }

}
