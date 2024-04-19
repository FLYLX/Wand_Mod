package com.flylx.wand_mod.block;

import com.flylx.wand_mod.Wand_mod;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.ToIntFunction;


public class modBlockRegistry {


    public static final Block WAND_TABLE = registerblock("wand_table",
            new WandTableBlock(AbstractBlock.Settings.of(Material.STONE).strength(4.0F)));

    public static final Block ALTAR_BLOCK = registerblock("altar_block",
            new AltarBlock(AbstractBlock.Settings.of(Material.WOOD).strength(4.0F)));

    public static final Block MAGIC_ORE = registerblock("magic_ore",
            new MagicOreBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().ticksRandomly().
                    luminance(modBlockRegistry.createLightLevelFromLitBlockState(32)).strength(5.0f, 6.0f)));

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) != false ? litLevel : 0;
    }

    public static Block registerblock(String name, Block block){
        return Registry.register(Registry.BLOCK,new Identifier(Wand_mod.ModID,name),block);
    }



}
