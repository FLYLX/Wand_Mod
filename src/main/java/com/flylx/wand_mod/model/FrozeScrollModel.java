package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.FrozeScroll;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FrozeScrollModel extends AnimatedGeoModel<FrozeScroll> {
    @Override
    public Identifier getModelResource(FrozeScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(FrozeScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/froze_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(FrozeScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }
}
