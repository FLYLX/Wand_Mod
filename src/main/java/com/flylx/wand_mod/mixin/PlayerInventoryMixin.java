package com.flylx.wand_mod.mixin;


import com.flylx.wand_mod.event.KeyInputHandler;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(at = {@At("HEAD")},
            method = {"scrollInHotbar(D)V"},
            cancellable = true)
    protected void OnScrollInHotbar(double scrollAmount, CallbackInfo ci){
        if(KeyInputHandler.ISPRESS_R){
            ci.cancel();
        }
    }

}
