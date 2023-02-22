package com.flylx.wand_mod.block;

import com.flylx.wand_mod.entity.WandTableEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;


import java.util.HashMap;
import java.util.Map;

public class WandTableBlock extends Block implements BlockEntityProvider {

        private static final Map<Item, Boolean> CONTENT_TO_POTTED = new HashMap<>(){
        {
            put(modItemRegistry.BASE_WAND,false);
        }
    };

    public VoxelShape makeShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.03125, 0.46875, 0.6875, 0.96875, 0.609375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.03125, 0.609375, 0.6875, 0.15625, 0.703125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.03125, 0.703125, 0.6875, 0.96875, 0.84375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.84375, 0.609375, 0.6875, 0.96875, 0.703125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.484375, 0, 0.4375, 0.5625, 1, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.31515292510187054, 4.415709791914368e-17, 0.01833372724981097, 0.44015292510187054, 0.9999999999999999, 0.04958372724981097));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.31515292510187054, 4.415709791914368e-17, 0.4245837272498111, 0.44015292510187054, 0.9999999999999999, 0.4558337272498111));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.31515292510187054, 0.96875, 0.04958372724981097, 0.44015292510187054, 0.9999999999999999, 0.4245837272498111));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.31515292510187054, 4.415709791914368e-17, 0.04958372724981097, 0.44015292510187054, 0.03125000000000004, 0.4245837272498111));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.28390292510187054, 4.415709791914368e-17, 0.01833372724981097, 0.31515292510187054, 0.9999999999999999, 0.4558337272498111));

        return shape;
    }

    public WandTableBlock(Settings settings) {
        super(settings);

    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WandTableEntity) {
            WandTableEntity wandTableEntity = (WandTableEntity) blockEntity;
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            boolean bl;

            bl = CONTENT_TO_POTTED.getOrDefault(item, true);


            boolean bl2 = this.isEmpty(wandTableEntity.content.getItem());


            if (bl != bl2) {
                if (bl2) {
                    wandTableEntity.setContent(item.getDefaultStack());


                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                } else {

                    ItemStack itemStack2 = wandTableEntity.content;
                    if (itemStack.isEmpty()) {
                        player.setStackInHand(hand, itemStack2);
                    } else if (!player.giveItemStack(itemStack2)) {
                        player.dropItem(itemStack2, false);
                    }
                    wandTableEntity.setContent(Items.AIR.getDefaultStack());
                }
                return ActionResult.success(world.isClient);
            } else {

                return ActionResult.CONSUME;
            }
        }
        return ActionResult.CONSUME;
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    private boolean isEmpty(Item item) {
        return item == Items.AIR;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {

        return new WandTableEntity(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockEntityProvider.super.getTicker(world, state, type);
    }





}
