package com.flylx.wand_mod.client;


import com.flylx.wand_mod.event.ClientPlayerTickHandler;
import com.flylx.wand_mod.render.*;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.entity.modEntityRegistry;
import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.item.modItemRegistry;

import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.screen.MagicScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import software.bernie.example.client.renderer.armor.GeckoArmorRenderer;
import software.bernie.example.registry.ItemRegistry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;


@Environment(EnvType.CLIENT)
public class Wand_modClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        GeoArmorRenderer.registerArmorRenderer(new ScrollBeltRenderer(), modItemRegistry.SCROLL_BELT_ITEM);


        GeoItemRenderer.registerItemRenderer(modItemRegistry.BASE_WAND,new BaseWandRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.FLAME_SCROLL,new FlameScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.EMPTY_SCROLL,new EmptyScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.FROZE_SCROLL,new FrozeScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.POISON_SCROLL,new PoisonScrollRenderer());
        BlockEntityRendererFactories.register(modEntityRegistry.WAND_TABLE, WandTableEntityRenderer::new);

        EntityRendererRegistry.register(modEntityRegistry.BASIC_MAGIC,(ctx) -> new BasicMagicRenderer(ctx));
        EntityRendererRegistry.register(modEntityRegistry.MAGIC_AREA,(ctx) -> new MagicCloudRenderer(ctx));

        HandledScreens.register(Wand_mod.MAGIC_SCREEN_HANDLER, MagicScreen::new);

        KeyInputHandler.register();

        HudRenderCallback.EVENT.register(new MagicSwitchHud());

        ModMessages.registerS2CPackets();

        ClientTickEvents.START_WORLD_TICK.register(new ClientPlayerTickHandler());


    }
}
