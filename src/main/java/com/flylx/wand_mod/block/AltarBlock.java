package com.flylx.wand_mod.block;

import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AltarBlock extends Block implements BlockEntityProvider  {

    //0为空气
    public static final IntProperty ITEM_PROPERTY = IntProperty.of("items", 0, 1024);

    public static final IntProperty HAS_ITEM = ITEM_PROPERTY;




    public static final Map<Item, Integer> content_map = new HashMap<>(){
        {
            put(Items.AIR,0);
            put(modItemRegistry.FLAME_SCROLL,1);
            put(modItemRegistry.FROZE_SCROLL,2);
            put(modItemRegistry.CLAW_SCROLL,3);
            put(modItemRegistry.CURE_SCROLL,4);
            put(modItemRegistry.POISON_SCROLL,5);
            put(Items.DIAMOND_BLOCK,6);
            put(Items.EMERALD_BLOCK,7);
            put(Items.CHORUS_FRUIT,8);
            put(Items.STRING,9);
            put(Items.WHITE_WOOL,10);
        }
    };

    private static final Map<Item, Boolean> CONTENT_TO_POTTED = new HashMap<>(){
        {
            put(modItemRegistry.FLAME_SCROLL,false);
            put(modItemRegistry.FROZE_SCROLL,false);
            put(modItemRegistry.CLAW_SCROLL,false);
            put(modItemRegistry.CURE_SCROLL,false);
            put(modItemRegistry.POISON_SCROLL,false);
            put(Items.DIAMOND_BLOCK,false);
            put(Items.EMERALD_BLOCK,false);
            put(Items.CHORUS_FRUIT,false);
            put(Items.STRING,false);
            put(Items.WHITE_WOOL,false);
        }
    };

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM);
    }

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(HAS_ITEM, 0));
    }

    //放置东西
    private static void putItem(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state,
                           ItemStack stack) {
        Item item = stack.getItem();
        int render_num = content_map.getOrDefault(item,0);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AltarEntity) {
            AltarEntity altarEntity = (AltarEntity)blockEntity;
            altarEntity.setContent(item.getDefaultStack());
            LogManager.getLogger().info(altarEntity.getContent());
            AltarBlock.setHasItem(world, pos, state, render_num);
            world.emitGameEvent((Entity)player, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    public static boolean putItemIfAbsent(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state,
                                       ItemStack stack) {
        if (state.get(HAS_ITEM) == 0) {
            if (!world.isClient) {

                AltarBlock.putItem(player, world, pos, state, stack);
            }
            return true;
        }
        return false;
    }
    //设置状态
    public static void setHasItem(World world, BlockPos pos, BlockState state, Integer hasItem) {
        world.setBlockState(pos, (state).with(HAS_ITEM, hasItem), Block.NOTIFY_ALL);
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
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    putItemIfAbsent(player,world,pos,state,itemStack);
                } else {

                    ItemStack itemStack2 = altarEntity.content;
                    if (itemStack.isEmpty()) {
                        player.setStackInHand(hand, itemStack2);
                    } else if (!player.giveItemStack(itemStack2)) {
                        player.dropItem(itemStack2, false);
                    }
                    AltarBlock.setHasItem(world, pos
                            , state, 0);
                    altarEntity.setContent(ItemStack.EMPTY);
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
