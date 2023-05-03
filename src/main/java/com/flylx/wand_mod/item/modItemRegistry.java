package com.flylx.wand_mod.item;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.block.WandTableBlock;
import com.flylx.wand_mod.block.modBlockRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;



public class modItemRegistry {


    public static final EmptyScroll EMPTY_SCROLL = (EmptyScroll) registerItem("empty_scroll",
            new EmptyScroll(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final FlameScroll FLAME_SCROLL = (FlameScroll) registerItem("flame_scroll",
            new FlameScroll(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final FrozeScroll FROZE_SCROLL = (FrozeScroll) registerItem("froze_scroll",
            new FrozeScroll(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final PoisonScroll POISON_SCROLL = (PoisonScroll) registerItem("poison_scroll",
            new PoisonScroll(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));


    public static final animated_base_wand BASE_WAND = (animated_base_wand) registerItem("base_wand",
            new animated_base_wand(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));

    public static final Item WAND_TABLE =
            registerItem("wand_table", new BlockItem(modBlockRegistry.WAND_TABLE,
                    new FabricItemSettings().group(ItemGroup.DECORATIONS))
            );

    public static Item registerItem(String name,Item item){
        return Registry.register(Registry.ITEM,new Identifier(Wand_mod.ModID,name),item);
    }

    public static Item registerItem(String name,BlockItem blockItemitem){
        return Registry.register(Registry.ITEM,new Identifier(Wand_mod.ModID,name),blockItemitem);
    }
}
