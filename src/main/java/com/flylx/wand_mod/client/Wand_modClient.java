package com.flylx.wand_mod.client;


import com.flylx.wand_mod.AnimatedItemRenderer;
import com.flylx.wand_mod.BasicMagicRenderer;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.entity.WandTableEntityRenderer;
import com.flylx.wand_mod.entity.modEntityRegistry;
import com.flylx.wand_mod.item.modItemRegistry;

import com.flylx.wand_mod.screen.MagicScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;


@Environment(EnvType.CLIENT)
public class Wand_modClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        GeoItemRenderer.registerItemRenderer(modItemRegistry.BASE_WAND,new AnimatedItemRenderer());
        EntityRendererRegistry.register(modEntityRegistry.BASIC_MAGIC,(ctx) -> new BasicMagicRenderer(ctx));
        BlockEntityRendererFactories.register(modEntityRegistry.WAND_TABLE, WandTableEntityRenderer::new);
        HandledScreens.register(Wand_mod.MAGIC_SCREEN_HANDLER, MagicScreen::new);

    }
}
