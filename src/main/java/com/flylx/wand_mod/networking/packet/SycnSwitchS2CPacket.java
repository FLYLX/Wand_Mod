package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;

public class SycnSwitchS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        //buf是接受的数据包
        ((IEntityDataSaver) client.player).getPersistentData().putFloat("switch", buf.readFloat());
    }
}
