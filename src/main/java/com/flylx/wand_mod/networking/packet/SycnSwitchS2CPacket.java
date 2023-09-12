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
        //buf是客户端接受的数据包
        if(MinecraftClient.getInstance().player!=null) {
            float degree = buf.readFloat();

            ((IEntityDataSaver) client.player).getPersistentData().putFloat("switch", degree);
        }

    }
}
