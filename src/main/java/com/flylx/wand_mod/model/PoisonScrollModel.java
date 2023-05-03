package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.FrozeScroll;
import com.flylx.wand_mod.item.PoisonScroll;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PoisonScrollModel extends AnimatedGeoModel<PoisonScroll> {
    @Override
    public Identifier getModelResource(PoisonScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(PoisonScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/poison_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(PoisonScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }
}
