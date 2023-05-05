package com.flylx.wand_mod.event;


import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;

public class ServerPlayerTickHandler implements ServerTickEvents.StartTick{

    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//            PacketByteBuf buffer = PacketByteBufs.create();
//            buffer.writeFloat(((IEntityDataSaver)player).getPersistentData().getFloat("switch"));
//            ServerPlayNetworking.send(player,ModMessages.SWITCH_SYNC_ID, buffer);
        }

    }


}

