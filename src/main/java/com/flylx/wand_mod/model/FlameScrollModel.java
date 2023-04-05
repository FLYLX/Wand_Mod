package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.FlameScroll;
import com.flylx.wand_mod.item.animated_base_wand;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlameScrollModel extends AnimatedGeoModel<FlameScroll> {
    @Override
    public Identifier getModelResource(FlameScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlameScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/flame_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(FlameScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }
}
