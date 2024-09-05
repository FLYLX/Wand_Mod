package com.flylx.wand_mod.render;

import com.flylx.wand_mod.mob.MagicPolymer;
import com.flylx.wand_mod.model.MagicPolymerModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MagicPolymerRenderer extends GeoProjectilesRenderer<MagicPolymer> {
    public MagicPolymerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MagicPolymerModel());
    }
}
