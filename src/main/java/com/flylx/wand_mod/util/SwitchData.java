package com.flylx.wand_mod.util;

import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import net.minecraft.server.network.ServerPlayerEntity;


public class SwitchData {
    public static void changeSwitch(IEntityDataSaver player,PacketByteBuf buf){
        float degree = buf.readFloat();
        NbtCompound nbt = player.getPersistentData();

//        float indegree = nbt.getFloat("switch");
//        if(indegree+degree>360f){
//            indegree = indegree+degree-360f;
//        }else if(indegree+degree<0) {
//            indegree = indegree+degree+360f;
//        }else{
//            indegree += degree;
//        }
        if(degree<=360.0f&&degree>=0) {
            nbt.putFloat("switch", degree);
//        MagicSwitchHud.change = 0;
        }
        syncThirst(nbt.getFloat("switch"), (ServerPlayerEntity) player);

    }

    public static void syncThirst(float degree, ServerPlayerEntity player) {

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(degree);
        ServerPlayNetworking.send(player, ModMessages.SWITCH_SYNC_ID, buffer);

    }


}
