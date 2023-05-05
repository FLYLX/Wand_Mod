package com.flylx.wand_mod;


import com.flylx.wand_mod.block.modBlockRegistry;
import com.flylx.wand_mod.entity.MagicAreaCloud;
import com.flylx.wand_mod.event.ServerPlayerTickHandler;
import com.flylx.wand_mod.mixin.PlayerEntityMixin;
import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.screen.MagicScreenHandler;
import com.flylx.wand_mod.item.modItemRegistry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.flylx.wand_mod.entity.modEntityRegistry;

public class Wand_mod implements ModInitializer {
    public static final String ModID = "wand_mod";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ScreenHandlerType<MagicScreenHandler> MAGIC_SCREEN_HANDLER =
            new ScreenHandlerType<>(MagicScreenHandler::new);

    @Override
    public void onInitialize() {

        Registry.register(Registry.SCREEN_HANDLER, new Identifier(ModID,"base_wand"), MAGIC_SCREEN_HANDLER);
        ModMessages.registerC2SPackets();

        ServerTickEvents.START_SERVER_TICK.register(new ServerPlayerTickHandler());
        new modItemRegistry();
        new modEntityRegistry();
        new modBlockRegistry();

    }
}
