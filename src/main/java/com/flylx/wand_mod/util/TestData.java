package com.flylx.wand_mod.util;

import net.minecraft.nbt.NbtCompound;

public class TestData {

    public static int addata(IEntityDataSaver player, int amount){
        NbtCompound nbtCompound = player.getPersistentData();
        nbtCompound.putInt("new",1);
        return 0;

    }
}
