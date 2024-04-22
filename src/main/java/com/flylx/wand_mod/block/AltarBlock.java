package com.flylx.wand_mod.block;

import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AltarBlock extends Block implements BlockEntityProvider  {
    private static final Map<Item, Boolean> CONTENT_TO_POTTED = new HashMap<>(){
        {
            put(modItemRegistry.FLAME_SCROLL,false);
            put(modItemRegistry.FROZE_SCROLL,false);
        }
    };
    public AltarBlock(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AltarEntity) {
            AltarEntity altarEntity = (AltarEntity) blockEntity;
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            boolean bl;

            bl = CONTENT_TO_POTTED.getOrDefault(item, true);


            boolean bl2 = this.isEmpty(altarEntity.content.getItem());


            if (bl != bl2) {
                if (bl2) {
                    altarEntity.setContent(item.getDefaultStack());


                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                } else {

                    ItemStack itemStack2 = altarEntity.content;
                    if (itemStack.isEmpty()) {
                        player.setStackInHand(hand, itemStack2);
                    } else if (!player.giveItemStack(itemStack2)) {
                        player.dropItem(itemStack2, false);
                    }
                    altarEntity.setContent(Items.AIR.getDefaultStack());
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

        return new AltarEntity(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockEntityProvider.super.getTicker(world, state, type);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        return BlockEntityProvider.super.getGameEventListener(world, blockEntity);
    }

    public VoxelShape makeShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.375, 0.125, 0.875, 0.4375, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.0625, 0.25, 0.75, 0.375, 0.75));

        return shape;
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }
}
