package com.flylx.wand_mod.item;

import com.flylx.wand_mod.block.AltarBlock;
import com.flylx.wand_mod.block.MagicOreBlock;
import com.flylx.wand_mod.block.modBlockRegistry;
import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.entity.MagicAreaCloud;
import com.flylx.wand_mod.entity.MagicShieldEffect;
import com.flylx.wand_mod.mob.MagicPolymer;
import com.flylx.wand_mod.mob.modMobRegistry;
import com.flylx.wand_mod.particle.modParticleRegistry;
import com.flylx.wand_mod.sound.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicDust extends Item {
    Map<List<Item>,Item> spawnMap = new HashMap<>();

    private enum State {
        None,
        Generate, // 生成特效
        Blooming  // 绽放特效
    }

    private State state = State.None;

    BlockPos blockPos;
    int spawn_age = 0;
    int break_age = 0;
    Item dropItem ;

    Block[] randFlower = {
            Blocks.SUNFLOWER,
            Blocks.LILAC,
            Blocks.ROSE_BUSH,
            Blocks.PEONY,
    };

    private Map<List<Item>,Item> getSpawnMap(){
        if (this.spawnMap.isEmpty()) {
            Item[][] itemStacks = {
                    {modItemRegistry.FLAME_SCROLL, modItemRegistry.FROZE_SCROLL, modItemRegistry.CLAW_SCROLL,
                            modItemRegistry.CURE_SCROLL, modItemRegistry.POISON_SCROLL, Items.DIAMOND_BLOCK,
                            Items.EMERALD_BLOCK, modItemRegistry.STONE_SCROLL},
                    {modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL, Items.TNT, Items.TNT,
                            Items.NETHER_STAR, Items.DIAMOND_BLOCK, Items.GOLDEN_APPLE, modItemRegistry.MAGIC_ORE},

                    {Items.STRING},
                    {Items.WHITE_WOOL},

                    {modItemRegistry.FLAME_SCROLL},
                    {modItemRegistry.FROZE_SCROLL},
                    {modItemRegistry.POISON_SCROLL},
                    {modItemRegistry.CLAW_SCROLL},
                    {modItemRegistry.CURE_SCROLL},
                    {modItemRegistry.STONE_SCROLL},

                    {modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE,
                            modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE, modItemRegistry.MAGIC_ORE,},
                    {modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL,
                            modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL, modItemRegistry.FLAME_SCROLL,},
                    {modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL,
                            modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL, modItemRegistry.FROZE_SCROLL,},
                    {modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL,
                            modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL, modItemRegistry.POISON_SCROLL,},
                    {modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL,
                            modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL, modItemRegistry.CURE_SCROLL,},
                    {modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL,
                            modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL, modItemRegistry.STONE_SCROLL,},
                    {modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL,
                            modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL, modItemRegistry.CLAW_SCROLL,},

            };
            //生成表
            Item[] items1 = {
                    modItemRegistry.MAGIC_ORE,
                    modItemRegistry.MAGIC_SHIELD,

                    Items.COBWEB,
                    Items.STRING,

                    modItemRegistry.FROZE_SCROLL,
                    modItemRegistry.POISON_SCROLL,
                    modItemRegistry.CLAW_SCROLL,
                    modItemRegistry.CURE_SCROLL,
                    modItemRegistry.STONE_SCROLL,
                    modItemRegistry.FLAME_SCROLL,

                    //不代表真实产生物品
                    Items.AIR,
                    Items.FIRE_CHARGE,
                    Items.PACKED_ICE,
                    Items.POISONOUS_POTATO,
                    Items.SHEARS,
                    Items.STONE,
                    Items.ENDERMAN_SPAWN_EGG
            };

            for (int i = 0; i < items1.length; i++) {
                List<Item> list = new ArrayList<>();
                for (int j = 0; j < itemStacks[i].length; j++) {
                    list.add(itemStacks[i][j]);
                }
                list.sort(Comparator.comparingInt(Object::hashCode));
                this.spawnMap.put(list, items1[i]);
            }
        }

        return this.spawnMap;
    }

    public MagicDust(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        LivingEntity targetEntity = world.getClosestEntity(LivingEntity.class, TargetPredicate.createNonAttackable().setBaseMaxDistance(6.0), user, user.getX(), user.getY(), user.getZ(), user.getBoundingBox().expand(6.0, 2.0, 6.0));
        if(targetEntity!=null) {
            itemStack.decrement(1);
            for (int i = 0;i<10;i++){
                double xoffset = new Random().nextDouble() * 2 - 1;
                double yoffset = new Random().nextDouble() * 2 - 1;
                double zoffset = new Random().nextDouble() * 2 - 1;
                world.addParticle(ParticleTypes.SCULK_SOUL,user.getX()+xoffset,user.getY()+yoffset+1,user.getZ()+zoffset,0,0,0);
            }
            targetEntity.setVelocity(0.0d,1.0d,0.0d);
            targetEntity.setFrozenTicks(200);
            world.createExplosion(targetEntity, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), 1.0f, true, Explosion.DestructionType.NONE);
        }
        return TypedActionResult.consume(itemStack);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);
        if(spawn_age == 195&&blockPos!=null&&world.getBlockState(blockPos).isOf(modBlockRegistry.MAGIC_ORE)){
            world.playSound(null,blockPos, ModSounds.SPAWN_END, SoundCategory.BLOCKS,3f,1f);
        }
        if(!world.isClient){
            if (state == State.Generate) {
                spawn_age++;
                for (ServerPlayerEntity player : ((ServerWorld)world).getPlayers()) {
                    player.networkHandler.sendPacket(new ParticleS2CPacket(modParticleRegistry.MAGICSHIELD_PARTICLE,true,
                            (double)blockPos.getX()+0.5+ MathHelper.sin(spawn_age/10+(float)Math.PI/2)/spawn_age*100,
                            (double)blockPos.getY()+(double)spawn_age/100+1,
                            (double)blockPos.getZ()+0.5+ MathHelper.cos(spawn_age/10+(float)Math.PI/2)/spawn_age*100,
                            0,0,0,0,0
                            ));
                    player.networkHandler.sendPacket(new ParticleS2CPacket(modParticleRegistry.MAGICSHIELD_PARTICLE,true,
                            (double)blockPos.getX()+0.5+ MathHelper.sin(spawn_age/10)/spawn_age*100,
                            (double)blockPos.getY()+(double)spawn_age/100+1,
                            (double)blockPos.getZ()+0.5+ MathHelper.cos(spawn_age/10)/spawn_age*100,0,0,0,0,0
                    ));
                    if(spawn_age==195){
                        player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FLASH,true,
                                blockPos.getX() + 0.5f, blockPos.getY() + 3.5f,
                                blockPos.getZ() + 0.5f,0,0,0,0,0
                        ));
                    }
                }

            }
                if(spawn_age>200){
                    //生成过程
                    if(!world.isClient) {
                        if (dropItem != null) {
                            if(dropItem == Items.STRING||dropItem == modItemRegistry.MAGIC_ORE||dropItem == Items.AIR||dropItem == Items.FIRE_CHARGE||
                                    dropItem == Items.PACKED_ICE||dropItem == Items.POISONOUS_POTATO||dropItem == Items.SHEARS||dropItem == Items.STONE||
                                    dropItem == Items.ENDERMAN_SPAWN_EGG) {
                                //特殊生成
                                if(dropItem == Items.AIR||dropItem == Items.FIRE_CHARGE||dropItem == Items.PACKED_ICE||dropItem == Items.POISONOUS_POTATO||
                                        dropItem == Items.SHEARS||dropItem == Items.STONE||dropItem == Items.ENDERMAN_SPAWN_EGG){
                                   specialSpawn(dropItem,world,blockPos);
                                }else {
                                    world.spawnEntity(new ItemEntity(world, blockPos.getX() + 0.5f, blockPos.getY() + 3.5f,
                                            blockPos.getZ() + 0.5f,
                                            new ItemStack(dropItem, 16)));
                                }
                            }else{
                                world.spawnEntity(new ItemEntity(world, blockPos.getX() + 0.5f, blockPos.getY() + 3.5f,
                                        blockPos.getZ() + 0.5f,
                                        dropItem.getDefaultStack()));
                            }
                            dropItem = null;
                        }
                    }
                }
                if (spawn_age > 225) {
                    state = State.None;
                    spawn_age = 0;
                }
            }
            if (state == State.Blooming) {
                break_age++;
                if (break_age > 125) {
                    state = State.None;
                    break_age = 0;
                }
            }
    }

    public void specialSpawn(Item dropItem,World world,BlockPos blockPos){
        if(dropItem == Items.AIR){
            MagicPolymer magicGolemEntity = modMobRegistry.MAGIC_POLYMER.create(world);
            magicGolemEntity.setPosition(new Vec3d(blockPos.getX(),blockPos.getY()+1,blockPos.getZ()));
            world.spawnEntity(magicGolemEntity);
        }else if(dropItem == Items.FIRE_CHARGE){
            MagicAreaCloud magicAreaCloud = new MagicAreaCloud(world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
            magicAreaCloud.setRadius(25.0F);
            magicAreaCloud.setRadiusGrowth(-0.01f);
            magicAreaCloud.setDegree(30.0F);
            world.spawnEntity(magicAreaCloud);
        }else if(dropItem == Items.PACKED_ICE){
            MagicAreaCloud magicAreaCloud = new MagicAreaCloud(world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
            magicAreaCloud.setRadius(25.0F);
            magicAreaCloud.setRadiusGrowth(-0.01f);
            magicAreaCloud.setDegree(90.0F);
            world.spawnEntity(magicAreaCloud);
        }else if(dropItem == Items.POISONOUS_POTATO){
            MagicAreaCloud magicAreaCloud = new MagicAreaCloud(world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
            magicAreaCloud.setRadius(25.0F);
            magicAreaCloud.setRadiusGrowth(-0.01f);
            magicAreaCloud.setDegree(150.0F);
            world.spawnEntity(magicAreaCloud);
        }else if(dropItem == Items.SHEARS){
            //cure
            MagicAreaCloud magicAreaCloud = new MagicAreaCloud(world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
            magicAreaCloud.setRadius(25.0F);
            magicAreaCloud.setRadiusGrowth(-0.01f);
            magicAreaCloud.setDegree(210.0F);
            searchBlock(world,new BlockPos(blockPos.getX()-0.5,blockPos.getY()+0.5,blockPos.getZ()-0.5),90);
            world.spawnEntity(magicAreaCloud);
        }else if(dropItem == Items.STONE){
            MagicAreaCloud magicAreaCloud = new MagicAreaCloud(world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
            magicAreaCloud.setRadius(25.0F);
            magicAreaCloud.setRadiusGrowth(-0.01f);
            magicAreaCloud.setDegree(330.0F);
            world.spawnEntity(magicAreaCloud);
        }else if(dropItem == Items.ENDERMAN_SPAWN_EGG){
            MagicShieldEffect magicShieldEffect = new MagicShieldEffect(world, blockPos.getX(), blockPos.getY(),blockPos.getZ());
            magicShieldEffect.setCircle(20.0d);
            magicShieldEffect.setRadius(1.5f);
            magicShieldEffect.setRadiusGrowth(-1f);
            magicShieldEffect.setRestart(18);
            world.spawnEntity(magicShieldEffect);
        }
    }


    public void searchBlock(World world,BlockPos blockPos,int count){
        AtomicInteger atom_count = new AtomicInteger(count);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        Queue<BlockPos> queue = new LinkedList<>();
                        queue.offer(blockPos);
                        queue.offer(blockPos);
                        BlockPos blockPos1;
                        while (!(queue.size() == 1)&&atom_count.get()>0){
                            blockPos1 = queue.poll();
                            blockPos1 = new BlockPos((double)(blockPos1.getX()) , (double)(blockPos1.getY())+0.5, (double) (blockPos1.getZ()));
                            for(double i = -1;i<=1;i++){
                                for(double j = -1;j<=1;j++){
                                    for(double k = -1;k<=1;k++) {
                                        BlockPos blockPos2 = new BlockPos((double)blockPos1.getX() + i, (double)blockPos1.getY()+j, (double)blockPos1.getZ() + k);
                                        if(judgeDirt(world,blockPos2)) {
                                            Thread.sleep(100);
                                            queue.offer(blockPos2);
                                            atom_count.decrementAndGet();
                                        }
                                    }
                                }
                            }
                        }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean judgeDirt(World world,BlockPos blockPos){
        if((world.getBlockState(blockPos).getBlock() == Blocks.ROOTED_DIRT || world.getBlockState(blockPos).getBlock() == Blocks.DIRT||
                world.getBlockState(blockPos).getBlock() == Blocks.ROOTED_DIRT ||world.getBlockState(blockPos).getBlock() == Blocks.GRASS_BLOCK)&&
                world.getBlockState(new BlockPos(blockPos.getX(),blockPos.getY()+1,blockPos.getZ())).getBlock() == Blocks.AIR){
            int rand = new Random().nextInt(4);
            world.setBlockState(new BlockPos(blockPos.getX(),blockPos.getY()+1,blockPos.getZ()),randFlower[rand].getDefaultState());
            return true;
        }
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(state == State.Generate){
            return ActionResult.FAIL;
        }
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if(world.getBlockState(blockPos).isOf(modBlockRegistry.MAGIC_ORE)) {
            trySpawn(blockPos, world,context);
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

    private boolean trySpawn(BlockPos pos, World world, ItemUsageContext context) {
        List<BlockPattern> blockPatternList = getAltarPattern();
        List<AltarEntity> altarEntityList = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        BlockPattern.Result result;

        for (int i = (-1) * blockPatternList.size() / 2; i < blockPatternList.size() / 2 + 1; i++) {
            result = blockPatternList.get(i + blockPatternList.size() / 2).searchAround(world, pos.add(0, 1, i));
            if (result == null) {
                return false;
            }
            for (int j = 0; j < this.blockPatternList.get(i + blockPatternList.size() / 2).getWidth(); ++j) {
                CachedBlockPosition cachedBlockPosition = result.translate(j, 0, 0);
                if (cachedBlockPosition.getBlockEntity() instanceof AltarEntity) {
                    AltarEntity altarEntity = (AltarEntity) cachedBlockPosition.getBlockEntity();
                    if (!altarEntity.getContent().isOf(Items.AIR)) {
                        if (!world.isClient) {
                            for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                                player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.SPIT, true,
                                        altarEntity.getPos().getX() + 0.5,
                                        altarEntity.getPos().getY() + 1,
                                        altarEntity.getPos().getZ() + 0.5, 0, 0, 0, 0, 0
                                ));
                            }
                            items.add(altarEntity.getContent().getItem());
                            altarEntityList.add(altarEntity);
                        }
                    }
                }
            }
        }
        //测试用
        this.blockPos = pos;
        items.sort(Comparator.comparingInt(Object::hashCode));

        if (getSpawnMap().get(items) == null)
            return false;

        this.dropItem = getSpawnMap().get(items);

        for (AltarEntity altarEntity : altarEntityList) {
            altarEntity.setContent(Items.AIR.getDefaultStack());
            BlockState state = world.getBlockState(altarEntity.getPos());
            AltarBlock.setItem(world, altarEntity.getPos(), state, 0);
        }

        context.getStack().decrement(1);
        context.getWorld().playSound(null,context.getBlockPos(), ModSounds.SPAWN_ITEM, SoundCategory.BLOCKS,2f,1f);

        state = State.Generate;

        return true;
    }

    @Nullable
    private static List<BlockPattern> blockPatternList = null;

    private static List<BlockPattern> getAltarPattern() {
        if (blockPatternList == null) {
            blockPatternList = new ArrayList<>();

            String[] pattern = {
                    "~~#~#~~",
                    "~~~~~~~",
                    "#~___~#",
                    "~~___~~",
                    "#~___~#",
                    "~~~~~~~",
                    "~~#~#~~"
            };

            for (String s : pattern) {
                blockPatternList.add(
                        BlockPatternBuilder.start().aisle(s)
                                .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(modBlockRegistry.ALTAR_BLOCK)))
                                .where('_', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR)))
                                .where('~', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
                                .build());
            }
        }
        return blockPatternList;
    }

    public Box getBox(BlockPos blockPos,double x, double y, double z) {
        return new Box(blockPos.getX() - x, blockPos.getY()-y, blockPos.getZ() - z,
                blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
    }



}

