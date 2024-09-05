package com.flylx.wand_mod.client;


import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.entity.modEntityRegistry;
import com.flylx.wand_mod.event.ClientPlayerTickHandler;
import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.hud.MagicSwitchHud;
import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.mob.modMobRegistry;
import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.particle.MagicShieldParticle;
import com.flylx.wand_mod.particle.modParticleRegistry;
import com.flylx.wand_mod.render.*;
import com.flylx.wand_mod.screen.MagicHandScreen;
import com.flylx.wand_mod.screen.MagicScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;


@Environment(EnvType.CLIENT)
public class Wand_modClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //armor
        GeoArmorRenderer.registerArmorRenderer(new ScrollBeltRenderer(), modItemRegistry.SCROLL_BELT_ITEM);


        //item
        GeoItemRenderer.registerItemRenderer(modItemRegistry.BASE_WAND,new BaseWandRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.FLAME_SCROLL,new FlameScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.EMPTY_SCROLL,new EmptyScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.FROZE_SCROLL,new FrozeScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.POISON_SCROLL,new PoisonScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.CURE_SCROLL,new CureScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.CLAW_SCROLL,new ClawScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.STONE_SCROLL,new StoneScrollRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.WAND_BOX,new WandBoxRenderer());



        GeoItemRenderer.registerItemRenderer(modItemRegistry.MAGIC_SHIELD,new MagicShieldRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.WAND_CORE,new WandCoreRenderer());
        GeoItemRenderer.registerItemRenderer(modItemRegistry.SCROLL_STICK,new ScrollStickRenderer());


        //block
        BlockEntityRendererFactories.register(modEntityRegistry.WAND_TABLE, WandTableEntityRenderer::new);
        BlockEntityRendererFactories.register(modEntityRegistry.ALTAR, AltarRenderer::new);



        //render
        EntityRendererRegistry.register(modEntityRegistry.BASIC_MAGIC,(ctx) -> new BasicMagicRenderer(ctx));
        EntityRendererRegistry.register(modEntityRegistry.MAGIC_AREA,(ctx) -> new MagicCloudRenderer(ctx));
        EntityRendererRegistry.register(modEntityRegistry.MAGIC_SHIELD,(ctx) -> new MagicShieldEffectRenderer(ctx));

        EntityRendererRegistry.register(modMobRegistry.MAGIC_GOLEM_ENTITY,(ctx)->new MagicGolemEntityRenderer(ctx));

        EntityRendererRegistry.register(modMobRegistry.MAGIC_POLYMER,MagicPolymerRenderer::new);



        //screen
        HandledScreens.register(Wand_mod.MAGIC_SCREEN_HANDLER, MagicScreen::new);
        HandledScreens.register(Wand_mod.MAGIC_SCREEN_HAND_HANDLER, MagicHandScreen::new);

        //particle
        ParticleFactoryRegistry.getInstance().register(modParticleRegistry.MAGICSHIELD_PARTICLE,
                MagicShieldParticle.Factary::new);


        //keybind
        KeyInputHandler.register();

        //hud
        HudRenderCallback.EVENT.register(new MagicSwitchHud());

        //s2c c2s packets
        ModMessages.registerS2CPackets();


        //listener
        ClientTickEvents.START_WORLD_TICK.register(new ClientPlayerTickHandler());




    }
}
