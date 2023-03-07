package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.util.IEntityDataSaver;
import com.flylx.wand_mod.util.TestData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ExampleC2SPacket {
    //服务器处理客户端发送的信息
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){

        EntityType.COW.spawn((ServerWorld) player.world, null,null, player, player.getBlockPos(), SpawnReason.TRIGGERED, true,
                false);

        TestData.addata((IEntityDataSaver) player,1);


    }
}
