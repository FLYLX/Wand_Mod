package com.flylx.wand_mod.armor;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ScrollBeltSlot extends Slot {
    public ScrollBeltSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem().canBeNested();
    }

}
