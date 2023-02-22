package com.flylx.wand_mod;

import com.flylx.wand_mod.client.BaseWandModel;
import com.flylx.wand_mod.item.animated_base_wand;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<animated_base_wand> {
    public AnimatedItemRenderer() {
        super(new BaseWandModel());
    }

}
