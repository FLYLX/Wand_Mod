package com.flylx.wand_mod;


import com.flylx.wand_mod.block.modBlockRegistry;
import com.flylx.wand_mod.dataGeneration.ModLodeGeneration;
import com.flylx.wand_mod.entity.modEntityRegistry;
import com.flylx.wand_mod.event.ServerPlayerTickHandler;
import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.mob.modMobRegistry;
import com.flylx.wand_mod.networking.ModMessages;
import com.flylx.wand_mod.particle.modParticleRegistry;
import com.flylx.wand_mod.screen.MagicScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        //提前注册进游戏，不写在这里会导致有些东西没注册进去
        new modItemRegistry();
        new modEntityRegistry();
        new modBlockRegistry();
        new modMobRegistry();

        //生成矿脉
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("wand_mod", "overworld_magic_ore"),
                ModLodeGeneration.OVERWORLD_MAGIC_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("wand_mod", "overworld_magic_ore"),
                ModLodeGeneration.OVERWORLD_MAGIC_ORE_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier("wand_mod", "overworld_magic_ore")));


        modMobRegistry.registryAttribute();
        modParticleRegistry.registerParticles();



    }
}
