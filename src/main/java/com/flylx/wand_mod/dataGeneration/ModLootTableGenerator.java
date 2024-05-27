package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.block.modBlockRegistry;
import com.flylx.wand_mod.item.modItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ModLootTableGenerator extends SimpleFabricLootTableProvider {
    public ModLootTableGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.BLOCK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
        //wand_table
        identifierBuilderBiConsumer.accept(new Identifier(Wand_mod.ModID,"blocks/wand_table"),
                BlockLootTableGenerator.drops(modBlockRegistry.WAND_TABLE));
        //magic_dust
        identifierBuilderBiConsumer.accept(new Identifier(Wand_mod.ModID,"blocks/magic_ore"),
                BlockLootTableGenerator.drops(modItemRegistry.MAGIC_DUST).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F,3.0F))));
        //altar
        identifierBuilderBiConsumer.accept(new Identifier(Wand_mod.ModID,"blocks/altar_block"),
                BlockLootTableGenerator.drops(modBlockRegistry.ALTAR_BLOCK));

    }

}
