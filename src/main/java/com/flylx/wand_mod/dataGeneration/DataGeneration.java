package com.flylx.wand_mod.dataGeneration;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(ModLootTableGenerator::new);
        fabricDataGenerator.addProvider(ModAdvancementsProvider::new);
        fabricDataGenerator.addProvider(ModRecipeGeneration::new);
        fabricDataGenerator.addProvider(ChestLootTables::new);
        fabricDataGenerator.addProvider(ModMobLootGenerator::new);
    }


}



