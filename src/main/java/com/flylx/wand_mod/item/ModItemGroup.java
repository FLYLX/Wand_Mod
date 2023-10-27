package com.flylx.wand_mod.item;

import com.flylx.wand_mod.Wand_mod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup WAND_MAGIC = FabricItemGroupBuilder.build(
            new Identifier(Wand_mod.ModID,"wand_mod"),()-> new ItemStack(modItemRegistry.BASE_WAND)
    );
}
