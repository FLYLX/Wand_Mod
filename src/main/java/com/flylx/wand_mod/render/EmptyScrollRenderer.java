package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.EmptyScroll;
import com.flylx.wand_mod.model.EmptyScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EmptyScrollRenderer extends GeoItemRenderer<EmptyScroll> {

    public EmptyScrollRenderer() {
        super(new EmptyScrollModel());
    }
}
