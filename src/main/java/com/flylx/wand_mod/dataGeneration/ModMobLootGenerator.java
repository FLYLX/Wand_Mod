package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.modItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ModMobLootGenerator extends SimpleFabricLootTableProvider {
    public ModMobLootGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextTypes.ENTITY);
    }



    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {


        ///setblock ~ ~ ~ minecraft:chest{LootTable:"wand_mod:chest/magic_loot"}
        //fire
        NbtCompound nbtCompound_fire = new NbtCompound();
        nbtCompound_fire.putString("golem_types","fire");
        NbtPredicate nbtPredicate_fire = new NbtPredicate(nbtCompound_fire);
        //froze
        NbtCompound nbtCompound_froze = new NbtCompound();
        nbtCompound_froze.putString("golem_types","froze");
        NbtPredicate nbtPredicate_froze = new NbtPredicate(nbtCompound_froze);
        //poison
        NbtCompound nbtCompound_poison = new NbtCompound();
        nbtCompound_froze.putString("golem_types","froze");
        NbtPredicate nbtPredicate_poison = new NbtPredicate(nbtCompound_poison);
        //end
        NbtCompound nbtCompound_end = new NbtCompound();
        nbtCompound_froze.putString("golem_types","end");
        NbtPredicate nbtPredicate_end = new NbtPredicate(nbtCompound_end);
        //stone
        NbtCompound nbtCompound_stone = new NbtCompound();
        nbtCompound_froze.putString("golem_types","stone");
        NbtPredicate nbtPredicate_stone = new NbtPredicate(nbtCompound_stone);
        identifierBuilderBiConsumer.accept(new Identifier(Wand_mod.ModID,"entities/magic_golem"), LootTable.builder()
                .pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0F,3.0f))
                        .with(ItemEntry.builder(modItemRegistry.CLAW_SCROLL).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,EntityPredicate.Builder.create().nbt(nbtPredicate_end)))
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.POISON_SCROLL).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,EntityPredicate.Builder.create().nbt(nbtPredicate_poison)))
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FROZE_SCROLL).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,EntityPredicate.Builder.create().nbt(nbtPredicate_froze)))
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.STONE_SCROLL).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,EntityPredicate.Builder.create().nbt(nbtPredicate_stone)))
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FLAME_SCROLL).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,EntityPredicate.Builder.create().nbt(nbtPredicate_fire)))
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))))

        );

        identifierBuilderBiConsumer.accept(new Identifier(Wand_mod.ModID,"entities/magic_polymer"),LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(7.0F,10.0F))
                        .with(ItemEntry.builder(modItemRegistry.CLAW_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.POISON_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FROZE_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.STONE_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.FLAME_SCROLL)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                        .with(ItemEntry.builder(modItemRegistry.MAGIC_DUST)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F,5.0F)))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(modItemRegistry.WAND_CORE))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))

        );
    }
}
