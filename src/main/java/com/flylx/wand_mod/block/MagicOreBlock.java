package com.flylx.wand_mod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MagicOreBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.of("lit");

    public MagicOreBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        MagicOreBlock.light(state, world, pos);
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.bypassesSteppingEffects()) {
            MagicOreBlock.light(state, world, pos);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            MagicOreBlock.spawnParticles(world, pos);
        } else {
            MagicOreBlock.light(state, world, pos);
        }
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof BlockItem && new ItemPlacementContext(player, hand, itemStack, hit).canPlace()) {
            return ActionResult.PASS;
        }
        return ActionResult.SUCCESS;
    }

    private static void light(BlockState state, World world, BlockPos pos) {
        MagicOreBlock.spawnParticles(world, pos);
        if (!state.get(LIT).booleanValue()) {
            world.setBlockState(pos, (BlockState)state.with(LIT, true), Block.NOTIFY_ALL);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(LIT);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT).booleanValue()) {
            world.setBlockState(pos, (BlockState)state.with(LIT, false), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, stack, dropExperience);
        if (dropExperience && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int i = 1 + world.random.nextInt(5);
            this.dropExperience(world, pos, i);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT).booleanValue()) {
            MagicOreBlock.spawnParticles(world, pos);
        }
    }

    public static void spawnParticles(World world, BlockPos pos) {
        Random random = world.random;

        int randomValue = random.nextInt(16777216); // 生成一个0~16777215的随机整数
        Vec3f RED = new Vec3f(Vec3d.unpackRgb(randomValue));
        double d = 0.5625;
        DustParticleEffect DEFAULT = new DustParticleEffect(RED, 1.0f);
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.offset(direction);
            if (world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) continue;
            Direction.Axis axis = direction.getAxis();
            double e = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double)direction.getOffsetX() : (double)random.nextFloat();
            double f = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double)direction.getOffsetY() : (double)random.nextFloat();
            double g = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double)direction.getOffsetZ() : (double)random.nextFloat();
            world.addParticle(DEFAULT, (double)pos.getX() + e, (double)pos.getY() + f,
                    (double)pos.getZ() + g, 255.0, 0.0, 0.0);
        }
    }


}
