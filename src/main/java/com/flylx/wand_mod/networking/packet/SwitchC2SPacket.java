package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.util.IEntityDataSaver;
import com.flylx.wand_mod.util.SwitchData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SwitchC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){

            SwitchData.changeSwitch((IEntityDataSaver) player,buf);



    }

}
