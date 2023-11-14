package com.flylx.wand_mod.entity;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.block.FertilizerBlock;

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

    private int restart = 3;


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
        this.getDataTracker().startTracking(RADIUS, Float.valueOf(0.5f));
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
    }

    @Override
    public float getRadiusGrowth() {
        return this.radiusGrowth;
    }

    @Override
    public void setRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("RadiusPerTick", this.radiusGrowth);
        nbt.putFloat("Radius", this.getRadius());
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
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



        for (LivingEntity livingEntity : list) {
//                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE);
//                statusEffectInstance.getEffectType()
//                        .applyInstantEffect(this,this.getOwner(),livingEntity,statusEffectInstance.getAmplifier(),0.5);
            livingEntity.damage(DamageSource.GENERIC,0);

        }

        searchBlock();

        this.setRadius(this.getRadius() + this.getRadiusGrowth());
        if(owner!=null) {
            setPosition(owner.getX(), owner.getY(), owner.getZ());
        }
        if (restart!=0){
            if(this.getRadius()>32.0f){
                this.setRadiusGrowth(-0.5f);
                restart = restart-1;
            }else if(this.getRadius()<2.0f){
                this.setRadiusGrowth(0.5f);
                restart = restart-1;
            }
        }
        if(this.getRadius()<0.5f||this.getRadius()>34.0f){
            discard();
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
}
