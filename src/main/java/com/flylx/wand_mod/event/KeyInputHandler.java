package com.flylx.wand_mod.event;

import com.flylx.wand_mod.networking.ModMessages;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_WAND = "key.com.flylx.wand_mod";
    public static final String KEY_MAGIC_SWITCH = "key.wand_mod.switch_magic";

    public static KeyBinding switch_magic_key;

    public static Boolean ISPRESS_R = false;

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





    }
    public static void register(){
        switch_magic_key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_MAGIC_SWITCH,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_WAND
        ));

        registerKeyInputs();
    }
}
