package com.flylx.wand_mod.networking.packet;

import com.flylx.wand_mod.item.modItemRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;
import java.util.List;

public class RapidEquipC2Spacket {

    private static List<Item> list = Arrays.asList(modItemRegistry.FROZE_SCROLL,modItemRegistry.FLAME_SCROLL,
            modItemRegistry.POISON_SCROLL,modItemRegistry.CLAW_SCROLL,modItemRegistry.CURE_SCROLL);
    public static void receive(MinecraftServer server, ServerPlayerEntity user, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        if (user.getEquippedStack(EquipmentSlot.CHEST).isOf(modItemRegistry.SCROLL_BELT_ITEM)&&user.getOffHandStack().isOf(Items.AIR)) {

            DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
            Inventories.readNbt(user.getEquippedStack(EquipmentSlot.CHEST).getNbt(),inventory);
            checkInventory(inventory,user);
            NbtCompound nbtCompound = new NbtCompound();
            Inventories.writeNbt(nbtCompound,inventory);
            user.getEquippedStack(EquipmentSlot.CHEST).setNbt(nbtCompound);
        }
    }

    public static DefaultedList<ItemStack> checkInventory(DefaultedList<ItemStack> inventory,
                                               ServerPlayerEntity serverPlayerEntity){
        for(int i = 0;i<inventory.size();i++){

            if(!inventory.get(i).isOf(Items.AIR)&&list.contains(inventory.get(i).getItem())&&serverPlayerEntity.getOffHandStack().isOf(Items.AIR)){
                serverPlayerEntity.getInventory().setStack(PlayerInventory.OFF_HAND_SLOT,inventory.get(i));
                inventory.set(i,ItemStack.EMPTY);
            }
        }

        return inventory;
    }
}
