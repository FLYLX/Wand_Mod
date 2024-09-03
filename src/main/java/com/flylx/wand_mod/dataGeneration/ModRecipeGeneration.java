package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.item.modItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeGeneration extends FabricRecipeProvider {
    public ModRecipeGeneration(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        //shapeless recipe
        ShapelessRecipeJsonBuilder.create(modItemRegistry.MAGIC_DUST, 1).input(Blocks.MUD).input(Items.WHEAT)
                .criterion("has_mud", RecipeProvider.conditionsFromItem(Blocks.MUD)).offerTo(exporter);

        //shape recipe
        ShapedRecipeJsonBuilder.create(modItemRegistry.SCROLL_STICK).pattern("bbb").pattern("ibi").pattern("bbb")
                .input('b', Items.DARK_OAK_WOOD)
                .input('i', Items.AIR)
                .criterion(FabricRecipeProvider.hasItem(Items.DARK_OAK_WOOD),
                        FabricRecipeProvider.conditionsFromItem(Items.AIR))
                .criterion(FabricRecipeProvider.hasItem(Items.AIR),
                        FabricRecipeProvider.conditionsFromItem(Items.DARK_OAK_WOOD))
                .offerTo(exporter);


        //baking recipe
    }
}
