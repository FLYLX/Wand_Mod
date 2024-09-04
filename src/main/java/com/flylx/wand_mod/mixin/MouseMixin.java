package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.event.MouseClick;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow public abstract boolean wasLeftButtonClicked();

    @Shadow @Final private MinecraftClient client = MinecraftClient.getInstance();

    @Shadow public abstract boolean wasRightButtonClicked();

    MagicSwitchHud magicSwitchHud = new MagicSwitchHud();
    @Inject(at = {@At("RETURN")}, method = {"onMouseScroll(JDD)V"})
    private void onOnMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if(KeyInputHandler.ISPRESS_R){
            ClientPlayerEntity player = client.player;
            if (player != null
            && (player.getMainHandStack().isOf(modItemRegistry.BASE_WAND)
            ||  player.getOffHandStack().isOf(modItemRegistry.BASE_WAND))) {
                if (vertical != 0) {
                    if (vertical > 0)
                        MagicSwitchHud.setHudDegree(MagicSwitchHud.getHubDegree() + 60);
                    else if (vertical < 0)
                        MagicSwitchHud.setHudDegree(MagicSwitchHud.getHubDegree() - 60);
                    if (client.player != null)
                        client.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                }
            }
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