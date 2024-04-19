package com.flylx.wand_mod.item;

import com.flylx.wand_mod.block.MagicOreBlock;
import com.flylx.wand_mod.block.modBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class MagicDust extends Item {
    public MagicDust(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if(blockState.getBlock() instanceof DyedCarpetBlock){
            BlockPos blockPos1 = new BlockPos(blockPos.getX(),blockPos.getY()-1,
                    blockPos.getZ());
            BlockState blockState1 = world.getBlockState(blockPos1);
            if(blockState1.getMaterial()== Material.WOOD&&blockState1.getBlock() instanceof PillarBlock){
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                world.setBlockState(blockPos1,Blocks.AIR.getDefaultState());
                for(int i = 0;i<5;i++) {
                    MagicOreBlock.spawnParticles(world, blockPos1);
                }
                world.setBlockState(blockPos1, modBlockRegistry.ALTAR_BLOCK.getDefaultState());
                context.getStack().decrement(1);
                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.FAIL;

    }

    public void check_spawn(BlockPos blockPos,World world){
        Box box  = getBox(blockPos,3,3,3);



    }

    public Box getBox(BlockPos blockPos,double x, double y, double z) {
        return new Box(blockPos.getX() - x, blockPos.getY()-y, blockPos.getZ() - z,
                blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
    }



}

