package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;

public class SycnSwitchS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        //buf是接受的数据包
        float degree = MagicSwitchHud.change;
        if(degree!=0.0f) {
            LogManager.getLogger().info(degree);
            float indegree = buf.readFloat();
            LogManager.getLogger().info("indegree"+indegree);

            if (indegree + degree > 360f) {
                indegree = indegree + degree - 360f;
            } else if (indegree + degree < 0) {
                indegree = indegree + degree + 360f;
            } else {
                indegree += degree;
            }
            MagicSwitchHud.change = 0.0f;
            LogManager.getLogger().info("indegree"+indegree);
            LogManager.getLogger().info("client player"+((IEntityDataSaver) client.player).getPersistentData().getFloat(
                    "switch"));
            ((IEntityDataSaver) client.player).getPersistentData().putFloat("switch", indegree);
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeFloat(indegree);
            ClientPlayNetworking.send(ModMessages.SWITCH_MAGIC, buffer);
            LogManager.getLogger().info("client player"+((IEntityDataSaver) client.player).getPersistentData().getFloat(
                    "switch"));
        }else{
            LogManager.getLogger().info(((IEntityDataSaver) client.player).getPersistentData().getFloat("switch"));

        }
    }
}
