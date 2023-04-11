package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.event.LeftClick;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.item.FlameScroll;
import net.minecraft.client.Mouse;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow public abstract boolean wasLeftButtonClicked();

    MagicSwitchHud magicSwitchHud = new MagicSwitchHud();
    @Inject(at = {@At("RETURN")}, method = {"onMouseScroll(JDD)V"})
    private void onOnMouseScroll(long long_1, double double_1, double double_2, CallbackInfo ci) {

        if(KeyInputHandler.ISPRESS_R){
            magicSwitchHud.setHudDegree((float) double_2*5f);

        }
    }

    @Inject(at = {@At("RETURN")}, method = {"onMouseButton"})
    private void onOnMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if(this.wasLeftButtonClicked()){
            LeftClick.isClick = true;
        }else{
            LeftClick.isClick = false;
        }
    }

}
