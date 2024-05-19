package com.flylx.wand_mod.networking;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.networking.packet.CallScreenC2SPacket;
import com.flylx.wand_mod.networking.packet.RapidEquipC2Spacket;
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
    public static final Identifier CALL_SCREEN = new Identifier(Wand_mod.ModID, "call_screen");
    public static final Identifier RAPID_EQUIP = new Identifier(Wand_mod.ModID, "rapid_equip");



    public static void registerC2SPackets(){
//        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        //客户端发送服务器的魔法切换数据
        ServerPlayNetworking.registerGlobalReceiver(SWITCH_MAGIC, SwitchC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CALL_SCREEN, CallScreenC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(RAPID_EQUIP, RapidEquipC2Spacket::receive);

    }
    public static void registerS2CPackets(){
        //服务器发送客户端的魔法切换数据
        ClientPlayNetworking.registerGlobalReceiver(SWITCH_SYNC_ID, SycnSwitchS2CPacket::receive);



    }
}
