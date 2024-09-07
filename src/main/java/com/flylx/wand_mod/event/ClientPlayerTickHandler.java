package com.flylx.wand_mod.event;

import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;


public class ClientPlayerTickHandler implements ClientTickEvents.StartWorldTick {
    @Override
    public void onStartTick(ClientWorld world) {
        //每tick同步一次switch数据
        //可以通过判定player来确认客户端是否进入游戏
        if (MinecraftClient.getInstance().player == null)
            return;

        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData();

        float degree = MagicSwitchHud.getHubDegree();
        nbt.putFloat("switch", degree);
        buf.writeFloat(degree);
        ClientPlayNetworking.send(ModMessages.SWITCH_MAGIC, buf);

    }
}
