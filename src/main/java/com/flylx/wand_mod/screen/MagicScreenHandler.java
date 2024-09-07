package com.flylx.wand_mod.screen;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.armor.ScrollBeltInventory;
import com.flylx.wand_mod.armor.ScrollBeltSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;


public class MagicScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    //type 1 is main hand , 2 is off hand , 3 is bag

    public MagicScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory,getInventory(playerInventory.getArmorStack(2),playerInventory));

    }

    public static  ScrollBeltInventory getInventory(ItemStack itemStack,PlayerInventory playerInventory){
        ScrollBeltInventory scrollBeltInventory;
        //优先主手
        scrollBeltInventory = new ScrollBeltInventory(itemStack);
        DefaultedList<ItemStack> inventory1 = DefaultedList.ofSize(27, ItemStack.EMPTY);
        Inventories.readNbt(itemStack.getNbt(),inventory1);
        scrollBeltInventory.setInventory(inventory1);

        return scrollBeltInventory;
    }

    public MagicScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Wand_mod.MAGIC_SCREEN_HANDLER, syncId);
        checkSize(inventory, 27);
        this.inventory = inventory;

        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);
        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;
        //Our inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new ScrollBeltSlot(inventory, l + m * 9, 8 + l * 18, 17 + m * 18));
            }
        }
        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }


    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return stack.getItem().canBeNested();
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {

            ItemStack newStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(index);
            if (slot != null && slot.hasStack()) {
                ItemStack originalStack = slot.getStack();
                newStack = originalStack.copy();
                if (index < this.inventory.size()) {
                    if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }

                if (originalStack.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }

            return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }




    @Override
    public ScreenHandlerType<?> getType() {
        return super.getType();
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

    }

}
