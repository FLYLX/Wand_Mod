package com.flylx.wand_mod.dataGeneration;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class ModAdvancementsProvider extends FabricAdvancementProvider {
    protected ModAdvancementsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        new Advancements().accept(consumer);
    }
}
