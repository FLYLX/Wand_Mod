package com.flylx.wand_mod.event;

import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;



public class ClientPlayerTickHandler implements ClientTickEvents.StartWorldTick {


    @Override
    public void onStartTick(ClientWorld world) {
        //每tick同步一次switch数据
        //可以通过判定player来确认客户端是否进入游戏
        ClientPlayNetworking.send(ModMessages.SWITCH_MAGIC, PacketByteBufs.create());

    }
}
