package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.EmptyScroll;
import com.flylx.wand_mod.item.FlameScroll;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmptyScrollModel extends AnimatedGeoModel<EmptyScroll> {
    @Override
    public Identifier getModelResource(EmptyScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(EmptyScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/empty_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(EmptyScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }


}
