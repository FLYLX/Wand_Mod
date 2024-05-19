package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.event.MouseClick;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow public abstract boolean wasLeftButtonClicked();

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract boolean wasRightButtonClicked();

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
            MouseClick.isLeftClick = true;

        }else{
            MouseClick.isLeftClick = true;
        }

        if(this.wasRightButtonClicked()){
            MouseClick.isRightClick = true;
            ClientPlayNetworking.send(ModMessages.RAPID_EQUIP, PacketByteBufs.create());


        }else{
            MouseClick.isRightClick = false;
        }
    }

}