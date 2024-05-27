package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.StoneScroll;
import com.flylx.wand_mod.model.StoneScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class StoneScrollRenderer extends GeoItemRenderer<StoneScroll> {
    public StoneScrollRenderer() {
        super(new StoneScrollModel());
    }

}