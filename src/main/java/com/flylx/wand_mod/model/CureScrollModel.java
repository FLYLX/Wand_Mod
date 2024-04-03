package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.CureScroll;
import com.flylx.wand_mod.item.FlameScroll;
import com.flylx.wand_mod.item.animated_base_wand;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CureScrollModel extends AnimatedGeoModel<CureScroll> {
    @Override
    public Identifier getModelResource(CureScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(CureScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/cure_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(CureScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/flame_scroll_animation.json");
    }
}
