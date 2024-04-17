package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.block.modBlockRegistry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

public class ModLodeGeneration {
    //矿物特点
    public static ConfiguredFeature<?, ?> OVERWORLD_MAGIC_ORE_CONFIGURED_FEATURE = new ConfiguredFeature
            (Feature.ORE, new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    modBlockRegistry.MAGIC_ORE.getDefaultState(),
                    4)); // 矿脉大小

    //矿物特征

    public static PlacedFeature OVERWORLD_MAGIC_ORE_FEATURE = new PlacedFeature(
            RegistryEntry.of(OVERWORLD_MAGIC_ORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(4), // 每个区块的矿脉数量
                    SquarePlacementModifier.of(), // 水平传播
                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))
            )); // 高度


}
