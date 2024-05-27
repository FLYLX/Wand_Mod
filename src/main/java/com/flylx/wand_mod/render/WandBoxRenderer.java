package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.WandBox;
import com.flylx.wand_mod.model.WandBoxModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class WandBoxRenderer extends GeoItemRenderer<WandBox> {
    public WandBoxRenderer() {
        super(new WandBoxModel());
    }
}
