package com.flylx.wand_mod.block;

import com.flylx.wand_mod.Wand_mod;
import net.minecraft.block.*;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class modBlockRegistry {

    public static final Block WAND_TABLE =(Block) registerblock("wand_table",
            new WandTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(4.0F)));


    public static Block registerblock(String name, Block block){
        return Registry.register(Registry.BLOCK,new Identifier(Wand_mod.ModID,name),block);
    }
}
