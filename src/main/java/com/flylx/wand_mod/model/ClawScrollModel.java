package com.flylx.wand_mod.model;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.ClawScroll;
import com.flylx.wand_mod.item.CureScroll;
import com.flylx.wand_mod.item.FlameScroll;
import com.flylx.wand_mod.item.animated_base_wand;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ClawScrollModel extends AnimatedGeoModel<ClawScroll> {
    @Override
    public Identifier getModelResource(ClawScroll object) {
        return new Identifier(Wand_mod.ModID,"geo/flame_scroll.geo.json");
    }

    @Override
    public Identifier getTextureResource(ClawScroll object) {
        return new Identifier(Wand_mod.ModID,"textures/item/claw_scroll.png");
    }

    @Override
    public Identifier getAnimationResource(ClawScroll animatable) {
        return new Identifier(Wand_mod.ModID,"animations/claw_scroll_animation.json");
    }
}
