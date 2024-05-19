package com.flylx.wand_mod.event;

import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_WAND = "key.com.flylx.wand_mod";
    public static final String KEY_MAGIC_SWITCH = "key.wand_mod.switch_magic";
    public static final String CALL_BELT_SCREEN = "key.wand_mod.belt_screen";

    public static KeyBinding switch_magic_key;
    public static KeyBinding call_screen_key;


    public static Boolean ISPRESS_R = false;
    public static Boolean ISPRESS_G = false;

    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(switch_magic_key.isPressed()){
                //创建数据包并发送
//                ClientPlayNetworking.send(ModMessages.EXAMPLE_ID, PacketByteBufs.create());

                ISPRESS_R = true;

            }else {

                ISPRESS_R = false;

            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(call_screen_key.isPressed()&&client.player.getEquippedStack(EquipmentSlot.CHEST).isOf(modItemRegistry.SCROLL_BELT_ITEM)){
                //创建数据包并发送
                ClientPlayNetworking.send(ModMessages.CALL_SCREEN, PacketByteBufs.create());

                ISPRESS_G = true;
//                MinecraftClient.getInstance().setScreen(new MagicScreen(new MagicScreenHandler(client.player.getId(),
//                    client.player.getInventory(),(ScrollBeltItem)client.player.getEquippedStack(EquipmentSlot.CHEST).getItem()),
//                        client.player.getInventory(), Text.literal("hello")));

            }else {

                ISPRESS_G = false;

            }
        });

    }
    public static void register(){
        switch_magic_key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_MAGIC_SWITCH,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_WAND
        ));
        call_screen_key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                CALL_BELT_SCREEN,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_WAND
        ));

        registerKeyInputs();
    }
}
