package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.FlameScroll;

import com.flylx.wand_mod.item.PoisonScroll;
import com.flylx.wand_mod.model.FlameScrollModel;
import com.flylx.wand_mod.model.PoisonScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class PoisonScrollRenderer extends GeoItemRenderer<PoisonScroll> {
    public PoisonScrollRenderer() {
        super(new PoisonScrollModel());
    }

}
