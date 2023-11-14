package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.MagicShield;
import com.flylx.wand_mod.model.MagicShieldModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MagicShieldRenderer extends GeoItemRenderer<MagicShield> {
    public MagicShieldRenderer() {
        super(new MagicShieldModel());
    }

}
