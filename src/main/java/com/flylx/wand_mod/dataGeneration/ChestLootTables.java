package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.item.modItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ChestLootTables extends SimpleFabricLootTableProvider {
    public ChestLootTables(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.CHEST);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
        ///setblock ~ ~ ~ minecraft:chest{LootTable:"wand_mod:chest/magic_loot"}
        identifierBuilderBiConsumer.accept(new Identifier("wand_mod","chest/magic_loot"), LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(5.0F))
                        .with(ItemEntry.builder(modItemRegistry.CURE_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FLAME_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FROZE_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.CLAW_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(Items.DIAMOND)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(Items.APPLE)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(32.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND_SWORD).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(40.0f,50.0f)))))
                );
    }
}
