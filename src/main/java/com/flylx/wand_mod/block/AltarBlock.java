package com.flylx.wand_mod.block;

import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class AltarBlock extends Block implements BlockEntityProvider  {

    //0为空气
    public static final IntProperty ITEM_PROPERTY = IntProperty.of("items", 0, 1024);

    public static final IntProperty HAS_ITEM = ITEM_PROPERTY;

    @Nullable
    private static Map<Item, Integer> CONTENT_MAP = null;

    @NotNull
    private static Map<Item, Integer> getContentMap() {
        if (CONTENT_MAP == null)
            CONTENT_MAP = new HashMap<>(){{
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
                put(Items.TNT,11);
                put(Items.NETHER_STAR,12);
                put(Items.GOLDEN_APPLE,13);
                put(Items.DARK_OAK_WOOD,14);
                put(modItemRegistry.MAGIC_ORE,15);
                put(modItemRegistry.WAND_CORE,16);
                put(modItemRegistry.STONE_SCROLL,17);
            }};
        return CONTENT_MAP;
    }

    @Nullable
    private static Set<Item> ALLOWED_ITEMS = null;

    @NotNull
    private static Set<Item> getAllowedItems() {
        if (ALLOWED_ITEMS == null)
            ALLOWED_ITEMS = new HashSet<>() {{
                add(modItemRegistry.FLAME_SCROLL);
                add(modItemRegistry.FROZE_SCROLL);
                add(modItemRegistry.CLAW_SCROLL);
                add(modItemRegistry.CURE_SCROLL);
                add(modItemRegistry.POISON_SCROLL);
                add(Items.DIAMOND_BLOCK);
                add(Items.EMERALD_BLOCK);
                add(Items.CHORUS_FRUIT);
                add(Items.STRING);
                add(Items.WHITE_WOOL);
                add(Items.TNT);
                add(Items.NETHER_STAR);
                add(Items.GOLDEN_APPLE);
                add(Items.DARK_OAK_WOOD);
                add(modItemRegistry.MAGIC_ORE);
                add(modItemRegistry.WAND_CORE);
                add(modItemRegistry.STONE_SCROLL);
            }};
        return ALLOWED_ITEMS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM);
    }

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(HAS_ITEM, 0));
    }

    //设置状态
    public static void setItem(World world, BlockPos pos, BlockState state, Integer item) {
        world.setBlockState(pos, (state).with(HAS_ITEM, item), Block.NOTIFY_ALL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof AltarEntity altarEntity))
            return ActionResult.CONSUME;

        ItemStack playerItemStack = player.getStackInHand(hand);
        ItemStack altarItemStack = altarEntity.content;

        boolean changed = false;

        if (!altarItemStack.isEmpty()) {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), altarItemStack));
            altarEntity.setContent(ItemStack.EMPTY);
            changed = true;
        }

        if (getAllowedItems().contains(playerItemStack.getItem())) {
            if (!player.getAbilities().creativeMode)
                playerItemStack.decrement(1);
            altarEntity.setContent(new ItemStack(playerItemStack.getItem()));
            changed = true;
        }

        if (changed) {
            world.setBlockState(pos, state.with(HAS_ITEM, getContentMap().getOrDefault(altarEntity.content.getItem(), 0)), Block.NOTIFY_ALL);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        return ActionResult.success(world.isClient);
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

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        dropContent(world,pos);
    }
    private void dropContent( World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AltarEntity) {
            AltarEntity altarEntity = (AltarEntity)blockEntity;
            ItemStack itemStack = altarEntity.getContent().copy();
            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 , pos.getY() + 1, (double)pos.getZ() + 0.5, itemStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
            altarEntity.setContent(ItemStack.EMPTY);
        }
    }


}
