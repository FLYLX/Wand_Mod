package com.flylx.wand_mod.mixin;

import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    private static final VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape CHECK_SHAPE = VoxelShapes.union(ABOVE_SHAPE);

    @Shadow public abstract ItemStack getStack();


    @Shadow public abstract ItemEntity copy();

    @Inject(at = @At("HEAD"), method = "tick()V")
    protected void ontick(CallbackInfo cir){
        if(!self().world.isClient) {
            //变成冰卷轴
            if (this.getStack().isOf(modItemRegistry.EMPTY_SCROLL)) {
                if(self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.ICE)){
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(),modItemRegistry.FROZE_SCROLL.getDefaultStack()));
                    self().world.setBlockState(new BlockPos(self().getPos()),Blocks.AIR.getDefaultState());
                    self().discard();
            //火卷轴
                }else if(self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.FIRE)){
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(),modItemRegistry.FLAME_SCROLL.getDefaultStack()));
                    self().world.setBlockState(new BlockPos(self().getPos()),Blocks.AIR.getDefaultState());
                    self().discard();

            //治愈代码
                }else if(self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.DRIPSTONE_BLOCK)){
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(),modItemRegistry.STONE_SCROLL.getDefaultStack()));
                    self().world.setBlockState(new BlockPos(self().getPos()),Blocks.AIR.getDefaultState());
                    self().discard();
                }
                //示例stream查找代码
                checkClawItem(self());
                //毒卷轴代码
                checkPoisonItem(self());
                //恢复代码
                checkCureItem(self());
            }

            //判定粉尘
            if(this.getStack().isOf(modItemRegistry.MAGIC_DUST)){
                BlockState blockState = Blocks.AIR.getDefaultState();
                if(self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.LAVA)||self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.WATER)||self().world.getBlockState(new BlockPos(self().getPos())).isOf(Blocks.POWDER_SNOW)){
                    blockState = self().world.getBlockState(new BlockPos(self().getPos()));
                    self().discard();
                    for(int i =-1;i<=1;i++){
                        for(int j = -1;j<=1;j++){
                            BlockPos blockPos = new BlockPos(self().getBlockPos().getX()+i, self().getBlockPos().getY(), self().getBlockPos().getZ()+j);
                            BlockState state = self().world.getBlockState(blockPos);
                            if(state.getBlock().getHardness()<=1){
                                BlockEntity blockEntity = blockState.hasBlockEntity() ? self().world.getBlockEntity(blockPos) : null;
                                Block.dropStacks(blockState, self().world, blockPos, blockEntity);
                                self().world.setBlockState(blockPos,blockState);
                                ServerPlayerEntity serverPlayerEntity = ((ServerPlayerEntity)(self().world.getClosestPlayer(self(),20.0d)));
                                if(serverPlayerEntity!=null) {
                                    serverPlayerEntity.networkHandler.sendPacket(
                                            new ParticleS2CPacket(ParticleTypes.SCULK_SOUL, true,
                                                    blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0, 0, 0, 0, 0)
                                    );
                                    serverPlayerEntity.networkHandler.sendPacket(
                                            new PlaySoundS2CPacket(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS,
                                                    blockPos.getX(), blockPos.getY() + 0.5d, blockPos.getZ(),
                                                    1,0,0
                                            )
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //示例stream查找代码
    public void checkClawItem(ItemEntity itemEntity){
        for (ItemEntity itemEntity1 : checkItemEntities(self().world, itemEntity)) {
            if(itemEntity1.getStack().isOf(Items.ENDER_EYE)){
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(), modItemRegistry.CLAW_SCROLL.getDefaultStack()));

                    itemEntity1.discard();
                    self().discard();
            }
        }
    }

    //示例stream查找代码
    public void checkPoisonItem(ItemEntity itemEntity){
        for (ItemEntity itemEntity1 : checkItemEntities(self().world, itemEntity)) {
            if(itemEntity1.getStack().isOf(Items.LINGERING_POTION)){
                if(PotionUtil.getPotion(itemEntity1.getStack().getNbt()).equals(Potions.STRONG_POISON)) {
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(), modItemRegistry.POISON_SCROLL.getDefaultStack()));

                    itemEntity1.discard();
                    self().discard();
                }
            }
        }
    }

    public void checkCureItem(ItemEntity itemEntity){
        for (ItemEntity itemEntity1 : checkItemEntities(self().world, itemEntity)) {
            if(itemEntity1.getStack().isOf(Items.LINGERING_POTION)){
                if(PotionUtil.getPotion(itemEntity1.getStack().getNbt()).equals(Potions.STRONG_REGENERATION)) {
                    self().world.spawnEntity(new ItemEntity(self().world,
                            self().getX(), self().getY(),
                            self().getZ(), modItemRegistry.CURE_SCROLL.getDefaultStack()));

                    itemEntity1.discard();
                    self().discard();
                }
            }
        }
    }

    private static List<ItemEntity> checkItemEntities(World world, ItemEntity itemEntity) {

        //示例stream查找代码
        return ABOVE_SHAPE.getBoundingBoxes().stream().flatMap(new Function<Box, Stream<? extends ItemEntity>>() {
            @Override
            public Stream<? extends ItemEntity> apply(Box box) {
                return world.getEntitiesByClass(ItemEntity.class,
                        box.offset(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ()),
                        EntityPredicates.VALID_ENTITY).stream();
            }
        }).collect(Collectors.toList());
    }

    private ItemEntity self() { return (ItemEntity)(Object)this; }
}
