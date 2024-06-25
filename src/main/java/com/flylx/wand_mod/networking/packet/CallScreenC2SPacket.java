package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.screen.MagicScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CallScreenC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity user, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {


        if (user.getEquippedStack(EquipmentSlot.CHEST).isOf(modItemRegistry.SCROLL_BELT_ITEM)) {
            NamedScreenHandlerFactory screenHandlerFactory = new SimpleNamedScreenHandlerFactory((syncId, inventory,
                                                                                                  player) -> new MagicScreenHandler(syncId, inventory), Text.literal("BeltInventory"));

            if (screenHandlerFactory != null) {
                // 这个调用会让服务器请求客户端开启合适的 Screenhandler
                user.openHandledScreen(screenHandlerFactory);
            }

        }
    }
}
