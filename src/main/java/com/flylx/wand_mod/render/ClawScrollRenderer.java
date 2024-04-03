package com.flylx.wand_mod.render;

import com.flylx.wand_mod.item.ClawScroll;
import com.flylx.wand_mod.item.CureScroll;
import com.flylx.wand_mod.item.FrozeScroll;
import com.flylx.wand_mod.model.ClawScrollModel;
import com.flylx.wand_mod.model.CureScrollModel;
import com.flylx.wand_mod.model.FrozeScrollModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class ClawScrollRenderer extends GeoItemRenderer<ClawScroll> {
    public ClawScrollRenderer() {
        super(new ClawScrollModel());
    }

}
