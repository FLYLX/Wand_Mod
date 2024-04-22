package com.flylx.wand_mod.item;

import com.flylx.wand_mod.block.MagicOreBlock;
import com.flylx.wand_mod.block.modBlockRegistry;
import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.particle.modParticleRegistry;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class MagicDust extends Item {
    List<BlockPattern> blockPatternList = new ArrayList<>();
    Map<List<Item>,Item> spawnMap = new HashMap<>();
    //0是无事发生，1是生成特效，2是爆炸特效
    private int state = 0;

    BlockPos blockPos;
    int spawn_age = 0;
    int break_age = 0;
    Item dropItem;

    public MagicDust(Settings settings) {

        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.consume(itemStack);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);
            if (state == 1) {
                spawn_age++;
                world.addParticle(modParticleRegistry.MAGICSHIELD_PARTICLE,
                        (double)blockPos.getX()+0.5+ MathHelper.sin(spawn_age/10)/spawn_age*100,
                        (double)blockPos.getY()+(double)spawn_age/100+1,
                        (double)blockPos.getZ()+0.5+ MathHelper.cos(spawn_age/10)/spawn_age*100,0,0,0);
                if(spawn_age==275){
                            world.addParticle(ParticleTypes.FLASH, blockPos.getX() + 0.5f, blockPos.getY() + 3.5f,
                                    blockPos.getZ() + 0.5f, 0, 0, 0);
                }

                if(spawn_age>280){
                    if(!world.isClient) {
                        if (dropItem != null) {
                            world.spawnEntity(new ItemEntity(world, blockPos.getX()+0.5f, blockPos.getY() + 3.5f,
                                    blockPos.getZ()+0.5f,
                                    dropItem.getDefaultStack()));
                            dropItem = null;
                        }
                    }
                }
                if (spawn_age > 300) {
                    state = 0;
                    spawn_age = 0;
                }
            }
            if (state == 2) {
                break_age++;
                if (break_age > 200) {

                    state = 0;
                    break_age = 0;
                }
            }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if(world.getBlockState(blockPos).isOf(modBlockRegistry.MAGIC_ORE)) {
            check_spawn(blockPos, world);
        }
        if (blockState.getBlock() instanceof DyedCarpetBlock) {
            BlockPos blockPos1 = new BlockPos(blockPos.getX(), blockPos.getY() - 1,
                    blockPos.getZ());
            BlockState blockState1 = world.getBlockState(blockPos1);
            if (blockState1.getMaterial() == Material.WOOD && blockState1.getBlock() instanceof PillarBlock) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                world.setBlockState(blockPos1, Blocks.AIR.getDefaultState());
                for (int i = 0; i < 5; i++) {
                    MagicOreBlock.spawnParticles(world, blockPos1);
                }
                world.setBlockState(blockPos1, modBlockRegistry.ALTAR_BLOCK.getDefaultState());
                context.getStack().decrement(1);
                return ActionResult.success(world.isClient);
            }
        }


        return ActionResult.FAIL;

    }
    public void check_spawn(BlockPos pos,World world){
        checkPattern(getAltarPattern(),pos,world);

    }

    private boolean checkPattern(List<BlockPattern> blockPatternList,BlockPos pos,World world){

        List<Item> items = new ArrayList<>();
        BlockPattern.Result result;
        for(int i = (-1)*blockPatternList.size()/2;i<blockPatternList.size()/2+1;i++){
            result = blockPatternList.get(i+blockPatternList.size()/2).searchAround(world,pos.add(0,1,i));
            if(result == null){
                return false;
            }
//            for (int j = (-1)*blockPatternList.get(i+blockPatternList.size()/2).getWidth()/2; j < this.blockPatternList.get(i+blockPatternList.size()/2).getWidth()/2 +1; ++j) {
            for (int j = 0; j < this.blockPatternList.get(i+blockPatternList.size()/2).getWidth(); ++j) {
                    CachedBlockPosition cachedBlockPosition = result.translate(j, 0, 0);
                    if(cachedBlockPosition.getBlockState() == modBlockRegistry.ALTAR_BLOCK.getDefaultState()){
                        if(cachedBlockPosition.getBlockEntity() instanceof AltarEntity){
                            AltarEntity altarEntity = (AltarEntity) cachedBlockPosition.getBlockEntity();
                            if(!altarEntity.getContent().isOf(Items.AIR)){
                                items.add(altarEntity.getContent().getItem());

                        }
                    }
                }
                }
        }
        //测试用
        this.blockPos = pos;
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.hashCode()-o2.hashCode();
            }
        });
//        Set<List<Item>> keySet = getSpawnMap().keySet();
//        List<Item> firstKey = keySet.iterator().next();
//        LogManager.getLogger().info(items);
//        LogManager.getLogger().info(firstKey);

//        LogManager.getLogger().info(items.equals(firstKey));
//        LogManager.getLogger().info((items.get(1)).equals(((Item)firstKey.get(1))));
        if(getSpawnMap().get(items) != null){
            this.dropItem = getSpawnMap().get(items);

            state = 1;
            return true;
        }else{
            return false;
        }

    }

    private Map<List<Item>,Item> getSpawnMap(){
        List<Item> list = new ArrayList<>();
        Item[][] itemStacks = {
                {modItemRegistry.FLAME_SCROLL,modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL,
                        modItemRegistry.FROZE_SCROLL},
                {},
                {},
        };
        Item[] items1 = {modItemRegistry.FROZE_SCROLL};
        if(this.spawnMap.isEmpty()){
            for(int i = 0;i<items1.length;i++){
                list.clear();
                for(int j = 0;j<itemStacks[i].length;j++){
                    list.add(itemStacks[i][j]);
                }
                list.sort(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return  o1.hashCode()-o2.hashCode();
                    }
                });
                this.spawnMap.put(list,items1[i]);

            }
        }
        return this.spawnMap;
    }


    private List<BlockPattern> getAltarPattern() {
        String[] pattern = {
                  "~~#~#~~"
                , "~~~~~~~"
                , "#~~~~~#"
                , "~~~~~~~"
                , "#~~~~~#"
                , "~~~~~~~"
                , "~~#~#~~"
        };
        if (this.blockPatternList.size() == 0) {
            for (int i = 0 ; i<pattern.length;i++){
                this.blockPatternList.add(BlockPatternBuilder.start().aisle(

                               pattern[i])
                        .where('#',
                                CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(modBlockRegistry.ALTAR_BLOCK)))
                        .where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build());
            }
        }
        return this.blockPatternList;
    }

    public Box getBox(BlockPos blockPos,double x, double y, double z) {
        return new Box(blockPos.getX() - x, blockPos.getY()-y, blockPos.getZ() - z,
                blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
    }



}

