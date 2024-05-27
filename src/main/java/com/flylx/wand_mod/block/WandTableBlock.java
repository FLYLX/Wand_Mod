package com.flylx.wand_mod.block;

import com.flylx.wand_mod.entity.WandTableEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class WandTableBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public static final IntProperty ITEM_PROPERTY = IntProperty.of("items", 0, 1024);

    public static final IntProperty HAS_ITEM = ITEM_PROPERTY;

    public static final Map<Item, Integer> content_map = new HashMap<>(){
        {
            put(Items.AIR,0);
            put(modItemRegistry.BASE_WAND,1);
        }
    };

    private static final Map<Item, Boolean> CONTENT_TO_POTTED = new HashMap<>(){
        {
            put(modItemRegistry.BASE_WAND,false);
        }
    };

    public WandTableBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(HAS_ITEM, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(HAS_ITEM);
    }


    //放置东西
    private static void putItem(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state,
                                ItemStack stack) {
        Item item = stack.getItem();
        int render_num = content_map.getOrDefault(item,0);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WandTableEntity) {
            WandTableEntity wandTableEntity = (WandTableEntity)blockEntity;
            wandTableEntity.setContent(stack);
            WandTableBlock.setHasItem(world, pos, state, render_num);
            world.emitGameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    public static boolean putItemIfAbsent(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state,
                                       ItemStack stack) {
        if (state.get(HAS_ITEM) == 0) {
            if (!world.isClient) {

                WandTableBlock.putItem(player, world, pos, state, stack);
            }
            return true;
        }
        return false;
    }
    //设置状态
    public static void setHasItem(World world, BlockPos pos, BlockState state, Integer hasItem) {
        world.setBlockState(pos, (state).with(HAS_ITEM, hasItem), Block.NOTIFY_ALL);
    }

    public VoxelShape makeShape(Direction dir){
        VoxelShape shape = VoxelShapes.empty();

        switch(dir) {
            case NORTH:
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4609375000000001, 0.03125, 0.3203124999999999, 0.6015625000000001, 0.96875, 0.4453124999999999));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.6953125, 0.03125, 0.3203124999999999, 0.8359375, 0.96875, 0.4453124999999999));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4296875000000001, 0, 0.4453124999999999, 0.8671875, 1, 0.5234374999999999));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.4453125, 0.15625, 1, 0.5703125));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.53125, 0, 0.4453125, 0.5625, 1, 0.5703125));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.5703125, 0.5625, 1, 0.6015625));

                return shape;
            case SOUTH:
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3984374999999999, 0.03125, 0.5546875000000001, 0.5390624999999999, 0.96875, 0.6796875000000001));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1640625, 0.03125, 0.5546875000000001, 0.3046875, 0.96875, 0.6796875000000001));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1328125, 0, 0.4765625000000001, 0.5703124999999999, 1, 0.5546875000000001));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.84375, 0, 0.4296875, 0.875, 1, 0.5546875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0, 0.4296875, 0.46875, 1, 0.5546875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0, 0.3984375, 0.875, 1, 0.4296875));


                return shape;
            case EAST:
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.03125, 0.46875, 0.6875, 0.96875, 0.609375));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.03125, 0.703125, 0.6875, 0.96875, 0.84375));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.484375, 0, 0.4375, 0.5625, 1, 0.875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4374999999999999, 0, 0.1328124999999999, 0.5624999999999999, 1, 0.1640624999999999));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4374999999999999, 0, 0.5390624999999999, 0.5624999999999999, 1, 0.5703124999999999));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4062499999999999, 0, 0.1328124999999999, 0.4374999999999999, 1, 0.5703124999999999));

                return shape;
            case WEST:
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.03125, 0.390625, 0.4375, 0.96875, 0.53125));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.03125, 0.15625, 0.4375, 0.96875, 0.296875));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0, 0.125, 0.515625, 1, 0.5625));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375000000000001, 0, 0.8359375000000001, 0.5625000000000001, 1, 0.8671875000000001));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375000000000001, 0, 0.4296875000000001, 0.5625000000000001, 1, 0.4609375000000001));
                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625000000000001, 0, 0.4296875000000001, 0.5937500000000001, 1, 0.8671875000000001));


                return shape;
            default:
                return VoxelShapes.fullCube();
        }


    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {

        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return super.mirror(state, mirror);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        Direction dir = state.get(FACING);
        return makeShape(dir);
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
                    if (!player.getAbilities().creativeMode) {
                        ItemStack stack = itemStack.copy();
                        putItemIfAbsent(player, world, pos, state, stack);
                        itemStack.decrement(1);
                    }else {
                        putItemIfAbsent(player, world, pos, state, itemStack);
                    }
                } else {

                    ItemStack itemStack2 = wandTableEntity.content;
                    if (itemStack.isEmpty()) {
                        player.setStackInHand(hand, itemStack2);
                    } else if (!player.giveItemStack(itemStack2)) {
                        player.dropItem(itemStack2, false);
                    }
                    WandTableBlock.setHasItem(world, pos
                            , state, 0);
                    wandTableEntity.setContent(ItemStack.EMPTY);
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

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        dropContent(world,pos);
    }

    private void dropContent( World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WandTableEntity) {
            WandTableEntity wandTableEntity = (WandTableEntity)blockEntity;
            ItemStack itemStack = wandTableEntity.getContent().copy();
            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 , pos.getY() + 1, (double)pos.getZ() + 0.5, itemStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
            wandTableEntity.setContent(ItemStack.EMPTY);
        }
    }
}
