package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.StoneScroll;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StoneScrollModel extends AnimatedGeoModel<StoneScroll> {
    @Override
    public Identifier getModelResource(StoneScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(StoneScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/stone_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(StoneScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }
}
