package com.flylx.wand_mod.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MagicScreenMixin {
    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo info) {
        System.out.println("这一行是示例模组mixin输出的！");
    }
}
