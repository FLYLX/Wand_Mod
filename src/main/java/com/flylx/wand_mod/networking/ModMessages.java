package com.flylx.wand_mod.networking;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.networking.packet.ExampleC2SPacket;
import com.flylx.wand_mod.networking.packet.SwitchC2SPacket;
import com.flylx.wand_mod.networking.packet.SycnSwitchS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    //定义事件
    public static final Identifier SWITCH_MAGIC = new Identifier(Wand_mod.ModID,"switch_magic");
    public static final Identifier EXAMPLE_ID = new Identifier(Wand_mod.ModID,"example");
    public static final Identifier SWITCH_SYNC_ID = new Identifier(Wand_mod.ModID, "switch_sync");



    public static void registerC2SPackets(){
//        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SWITCH_MAGIC, SwitchC2SPacket::receive);

    }
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(SWITCH_SYNC_ID, SycnSwitchS2CPacket::receive);


    }
}
