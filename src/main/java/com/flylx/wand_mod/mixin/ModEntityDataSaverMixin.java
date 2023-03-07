package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {

        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return this.persistentData;

    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteNbtMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir){

        if(persistentData!=null){
            nbt.put("wand_mod.mod_data",persistentData);
            LogManager.getLogger().info(("written"));
        }


    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadNbtMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains("wand_mod.mod_data")){
            persistentData = nbt.getCompound("wand_mod.mod_data");
            LogManager.getLogger().info(("read"));
            LogManager.getLogger().info(nbt.getCompound("wand_mod.mod_data").getFloat("switch"));
        }
    }
}
