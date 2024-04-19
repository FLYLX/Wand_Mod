package com.flylx.wand_mod.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class MagicShieldEffect extends AreaEffectCloudEntity{

    //追踪数据
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(MagicShieldEffect.class,
            TrackedDataHandlerRegistry.FLOAT);

    private float radiusGrowth;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;
    private final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList();
    private int restart ;
    private double parameter,parameter1,cumsum=0;


    Vec3d velocity;

    public MagicShieldEffect(EntityType<? extends AreaEffectCloudEntity> entityType, World world) {
        super(entityType, world);
    }
    public MagicShieldEffect(World world, double x, double y, double z) {
        //参考AreaEffectCloudEntity
        this(modEntityRegistry.MAGIC_SHIELD, world);
        this.setPosition(x, y, z);

    }

    @Override
    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.world instanceof ServerWorld && (entity = ((ServerWorld) this.world).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (LivingEntity) entity;
        }
        return this.owner;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(RADIUS, Float.valueOf(0.0f));
    }

    @Override
    public void setRadius(float radius) {
        if (!this.world.isClient) {
            this.getDataTracker().set(RADIUS, Float.valueOf(MathHelper.clamp(radius, 0.0f, 64.0f)));
        }
    }

    @Override
    public float getRadius() {
        return this.getDataTracker().get(RADIUS).floatValue();
    }



    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.radiusGrowth = nbt.getFloat("RadiusPerTick");
        this.setRadius(nbt.getFloat("Radius"));
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        this.setRestart(this.getRestart());

    }

    @Override
    public float getRadiusGrowth() {
        return this.radiusGrowth;
    }

    @Override
    public void setRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    public int getRestart(){
        return  this.restart;
    }

    public void setRestart(int restart){
        this.restart = restart;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("RadiusPerTick", this.radiusGrowth);
        nbt.putFloat("Radius", this.getRadius());
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        nbt.putInt("restart",this.getRestart());
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (RADIUS.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    @Override
    public void tick() {
        //逐渐减少范围
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
            List<ItemEntity> list1 = this.world.getNonSpectatingEntities(ItemEntity.class, this.getBoundingBox());
            List<PersistentProjectileEntity> list2 =
                    this.world.getNonSpectatingEntities(PersistentProjectileEntity.class, this.getBoundingBox());

            for (LivingEntity livingEntity : list) {
                    if (!world.isClient) {
                        if(!livingEntity.equals(owner)){
                            livingEntity.takeKnockback(0.2, this.getX() - livingEntity.getX(), this.getZ() - livingEntity.getZ());
                            livingEntity.setVelocity(livingEntity.getVelocity().add((livingEntity.getX() - this.getX()) / 10,
                            (livingEntity.getY() - this.getY()) / 10, (livingEntity.getZ() - this.getZ()) / 10));
                            if(livingEntity instanceof ServerPlayerEntity) {
                                setVelocity((livingEntity.getX() - this.getX()) / 10,
                                (livingEntity.getY() - this.getY()) / 10, (livingEntity.getZ() - this.getZ()) / 10);
                                ((ServerPlayerEntity) livingEntity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(livingEntity.getId(),
                                        velocity));
//                                world.addParticle(ParticleTypes.HEART, false,
//                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 0, 0);
//                                for (int j = 0; j < this.world.getPlayers().size(); ++j) {
//                                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.world.getPlayers().get(j);
//                                    if (!((ServerWorld)(this.world)).sendToPlayerIfNearby(serverPlayerEntity, false, x,
//                                            y, z,
//                                            particleS2CPacket)) continue;
//                                    ++i;
//                                }
                            }
                            ((ServerWorld)(this.world)).spawnParticles(ParticleTypes.FLASH,livingEntity.getX(),
                                    livingEntity.getY(),livingEntity.getZ(),1,0,0,0,1);
                        }
                    }
//                if (world.isClient) {
//                    if(!livingEntity.equals(owner)) {
//                        world.addParticle(ParticleTypes.HEART, false,
//                                livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 0, 0);
//                        LogManager.getLogger().info("RADIUS:"+getRadius());
//                        LogManager.getLogger().info("im alive");
//                        LogManager.getLogger().info("ovner:"+owner);
//                    }
//                }
            }
            for (ItemEntity itemEntity : list1) {
//                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE);
//                statusEffectInstance.getEffectType()
//                        .applyInstantEffect(this,this.getOwner(),livingEntity,statusEffectInstance.getAmplifier(),0.5);
                    setVelocity((itemEntity.getX()-this.getX())/10,
                            (itemEntity.getY()-this.getY())/10,(itemEntity.getZ()-this.getZ())/10);
                    itemEntity.setVelocity(velocity);


            }

            for(PersistentProjectileEntity persistentProjectileEntity:list2){
                setVelocity((persistentProjectileEntity.getX()-this.getX())/10,
                        (persistentProjectileEntity.getY()-this.getY())/10,(persistentProjectileEntity.getZ()-this.getZ())/10);
                persistentProjectileEntity.setVelocity(velocity);
            }

            searchBlock();


            this.setRadius(this.getRadius() + this.getRadiusGrowth());
            if (owner != null) {
                setPosition(owner.getX(), owner.getY(), owner.getZ());
            }
            if (restart != 0) {
                if (this.getRadius() > 16.0f) {
                    this.setRadiusGrowth(-1f);
                    restart = restart - 1;
                } else if (this.getRadius() <= 0.5f) {
                    this.setRadiusGrowth(1f);
                    restart = restart - 1;
                }
            }
            if (this.getRadius() <= 0.0f || this.getRadius() > 34.0f) {
                discard();
            }
            if(world.isClient){
                spawnShieldParticle(world);
            }else{
                //检测脚下方块
//                BlockPos blockPos = new BlockPos(getOwner().getBlockPos().getX(),getOwner().getBlockPos().getY()-3,
//                        getOwner().getBlockPos().getZ());
//                BlockState blockState = world.getBlockState(blockPos);
//

//                if(blockState.isSolidBlock(world,blockPos)&&getOwner().getVelocity().getY()<0&&getOwner().getPos().getY()-(double) blockPos.getY()<3.1d){
//                    setVelocity(getOwner().getVelocity().getX(),0.1, getOwner().getVelocity().getZ());
//                    ((ServerPlayerEntity) getOwner()).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(getOwner().getId(),
//                            velocity));
//                }
                if(getOwner()!=null) {
                    getOwner().fallDistance = 0.0f;
                }
            }



    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
    }

//    public EntityDimensions getDimensions(EntityPose pose) {
//        return EntityDimensions.changing(this.getRadius() * 2.0f, this.getRadius() * 2.0f);
//    }

    @Override
    protected Box calculateBoundingBox() {
        return getBoxAt(this.getX(),this.getY(),this.getZ());
    }

    public Box getBoxAt(double x, double y, double z) {
        float f = this.getRadius();
        float g = this.getRadius();
        return new Box(x - (double)f, y- (double)g, z - (double)f, x + (double)f, y + (double)g, z + (double)f);
    }

    public void searchBlock(){
        Iterator var = BlockPos.iterate(this.getBlockPos().add(this.getRadius(),this.getRadius(),this.getRadius()),
                this.getBlockPos().add(-this.getRadius(),-this.getRadius(),-this.getRadius())).iterator();
        while (var.hasNext()){
            BlockPos blockPos = (BlockPos) var.next();
            if(blockPos.isWithinDistance(this.getPos(),this.getRadius())){
                BlockState blockState = world.getBlockState(blockPos);
                if(blockState.getBlock() instanceof LeavesBlock||blockState.getBlock() instanceof PlantBlock||blockState.getBlock()==Blocks.SNOW){
                    BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(blockPos) : null;
                    Block.dropStacks(blockState, world, blockPos, blockEntity);
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(double x, double y, double z) {
        this.setVelocity(new Vec3d(x, y, z));
    }

    public void spawnShieldParticle(World world){
//ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ
        //once every right button
//        while(count>0){
//            for(int i = -1;i <= 1;i++){
//                for(int j = -1; j <= 1;j++){
//                    for(int k = -1;k <= 1;k++){
//                        world.addParticle(modParticleRegistry.MAGICSHIELD_PARTICLE,this.getX(),this.getY(),
//                                this.getZ(),i,j,k);
//                    }
//                }
//            }
//            count--;
//        }
        if(getRadius()>1&&world.getTickOrder()%1==0){
            LogManager.getLogger().info("radius:"+getRadius());
            for (double j = 180;j>=-180;j=j-45) {
                cumsum = cumsum + 0.5;
                parameter1 = Math.toRadians(j);

                for (int k = 0; k < 360; k = (int) (k + 60)) {

                    parameter = Math.toRadians(k);
                    LogManager.getLogger().info("consum:"+cumsum);
                    LogManager.getLogger().info("getRadiusGrowth:"+getRadiusGrowth());
                    world.addParticle(ParticleTypes.ELECTRIC_SPARK,
                            this.getX() + cumsum / 5 * Math.sin(parameter*random.nextFloat()+cumsum/100)*Math.cos(parameter1),
                            this.getY() + cumsum / 10 * Math.sin(parameter1) + random.nextFloat()*2 ,
                            this.getZ() + cumsum / 5 * Math.cos(parameter*random.nextFloat()+cumsum/100)*Math.cos(parameter1),
                            0, 0, 0);

                }
            }
//            for (double i = 0.4;i>0;i=i-0.2) {
//                    world.addParticle(ParticleTypes.ENCHANT,
//                            this.getX() + getRadius() / 10 * Math.sin(getRadius() + i + Math.PI / 2),
//                            this.getY()+j, this.getZ() + getRadius()/10 * Math.cos(getRadius() + i + Math.PI / 2),
//                            0, 0, 0);
//                    world.addParticle(ParticleTypes.ENCHANT,
//                            this.getX() + getRadius()  * Math.sin(getRadius() + i),
//                            this.getY()+j, this.getZ() + getRadius() * Math.cos(getRadius() + i),
//                            0, 0, 0);
//
//                }
            }
    }
}
