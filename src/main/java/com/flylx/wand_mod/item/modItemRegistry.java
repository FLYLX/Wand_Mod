package com.flylx.wand_mod.item;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.armor.ScrollBeltItem;
import com.flylx.wand_mod.block.modBlockRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class modItemRegistry {

    public static final ScrollBeltItem SCROLL_BELT_ITEM = (ScrollBeltItem) registerItem("scroll_belt",
            new ScrollBeltItem(ArmorMaterials.DIAMOND, EquipmentSlot.CHEST,
                    new Item.Settings().group(ModItemGroup.WAND_MAGIC)));

    public static final EmptyScroll EMPTY_SCROLL = (EmptyScroll) registerItem("empty_scroll",
            new EmptyScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final FlameScroll FLAME_SCROLL = (FlameScroll) registerItem("flame_scroll",
            new FlameScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final FrozeScroll FROZE_SCROLL = (FrozeScroll) registerItem("froze_scroll",
            new FrozeScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final PoisonScroll POISON_SCROLL = (PoisonScroll) registerItem("poison_scroll",
            new PoisonScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final CureScroll CURE_SCROLL = (CureScroll) registerItem("cure_scroll",
            new CureScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final ClawScroll CLAW_SCROLL = (ClawScroll) registerItem("claw_scroll",
            new ClawScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final StoneScroll STONE_SCROLL = (StoneScroll) registerItem("stone_scroll",
            new StoneScroll(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final MagicDust MAGIC_DUST = (MagicDust) registerItem("magic_dust",
            new MagicDust(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(64)));
    public static final WandBox WAND_BOX = (WandBox) registerItem("wand_box",
            new WandBox(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final ScrollStick SCROLL_STICK = (ScrollStick) registerItem("scroll_stick",
            new ScrollStick(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));


    public static final animated_base_wand BASE_WAND = (animated_base_wand) registerItem("base_wand",
            new animated_base_wand(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final MagicShield MAGIC_SHIELD = (MagicShield) registerItem("magic_shield",
            new MagicShield(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));
    public static final WandCore WAND_CORE = (WandCore) registerItem("wand_core",
            new WandCore(new FabricItemSettings().group(ModItemGroup.WAND_MAGIC).maxCount(1)));

    public static final Item WAND_TABLE =
            registerItem("wand_table", new BlockItem(modBlockRegistry.WAND_TABLE,
                    new FabricItemSettings().group(ModItemGroup.WAND_MAGIC))
            );
    public static final Item MAGIC_ORE = registerItem("magic_ore",new BlockItem(modBlockRegistry.MAGIC_ORE,
            new FabricItemSettings().group(ModItemGroup.WAND_MAGIC)));
    public static final Item ALTAR_BLOCK = registerItem("altar_block",new BlockItem(modBlockRegistry.ALTAR_BLOCK,
            new FabricItemSettings().group(ModItemGroup.WAND_MAGIC)));


    public static Item registerItem(String name,Item item){
        return Registry.register(Registry.ITEM,new Identifier(Wand_mod.ModID,name),item);
    }

    public static Item registerItem(String name,BlockItem blockitem){
        ((BlockItem)blockitem).appendBlocks(Item.BLOCK_ITEMS, blockitem);
        return Registry.register(Registry.ITEM,new Identifier(Wand_mod.ModID,name),blockitem);
    }
}
