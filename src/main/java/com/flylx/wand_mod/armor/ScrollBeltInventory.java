package com.flylx.wand_mod.armor;

import com.flylx.wand_mod.screen.ImplementedInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class ScrollBeltInventory implements ImplementedInventory  {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final ItemStack itemStack ;
    public ScrollBeltInventory(ItemStack itemStack){
        this.itemStack = itemStack;
    }
    @Override
    public DefaultedList<ItemStack> getItems() {

//        LogManager.getLogger().info(isSameInventory(inventory,inventory1));
//        if(!isSameInventory(inventory,inventory1)){
//
//
//            return inventory;
//        }
        NbtCompound nbtCompound = new NbtCompound();
        this.itemStack.setNbt(Inventories.writeNbt(nbtCompound,inventory));
        return inventory;
    }


    public void setInventory(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }


//        if(player instanceof ServerPlayerEntity){
//            ((ServerPlayerEntity)player).closeScreenHandler();
//            ((ServerPlayerEntity)player).closeHandledScreen();
//        }else{
//            ((ClientPlayerEntity)player).closeHandledScreen();
//        }



}
