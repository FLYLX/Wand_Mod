package com.flylx.wand_mod.mixin;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.flylx.wand_mod.item.modItemRegistry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = {@At("RETURN")},
            method = {"dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;"})
    protected void ondropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership,
                            CallbackInfoReturnable<ItemEntity> cir) {




    }
    @Inject(at = {@At("RETURN")},
            method = {"tick()V"})
    protected void ontick(CallbackInfo cir) {


    }




}
