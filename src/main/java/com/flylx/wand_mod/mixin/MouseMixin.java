package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import net.minecraft.client.Mouse;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    MagicSwitchHud magicSwitchHud = new MagicSwitchHud();
    @Inject(at = {@At("RETURN")}, method = {"onMouseScroll(JDD)V"})
    private void onOnMouseScroll(long long_1, double double_1, double double_2, CallbackInfo ci) {
        if(KeyInputHandler.ISPRESS_R){
            magicSwitchHud.setQuaternion(Vec3f.POSITIVE_Z.getRadialQuaternion((float) double_2/100));

        }
    }
}
