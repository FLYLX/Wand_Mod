package com.flylx.wand_mod.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MagicAreaCloud extends AreaEffectCloudEntity  {


    private static final TrackedData<Float> RADIUS = DataTracker.registerData(MagicAreaCloud.class,
            TrackedDataHandlerRegistry.FLOAT);
    private float radiusGrowth;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;


    public MagicAreaCloud(EntityType<? extends MagicAreaCloud> entityType, World world) {
        //参考AreaEffectCloudEntity
        super((EntityType<? extends AreaEffectCloudEntity>) entityType, world);
        this.age = 20;
        LogManager.getLogger().info("area:open");
    }
    public MagicAreaCloud(World world, double x, double y, double z) {
        //参考AreaEffectCloudEntity
        this(modEntityRegistry.MAGIC_AREA, world);
        this.setPosition(x, y, z);
    }

    @Override
    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.world instanceof ServerWorld && (entity = ((ServerWorld)this.world).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (LivingEntity)entity;
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
            this.getDataTracker().set(RADIUS, Float.valueOf(MathHelper.clamp(radius, 0.0f, 32.0f)));
        }
    }

    @Override
    public float getRadius() {
        return this.getDataTracker().get(RADIUS).floatValue();
    }

    @Override
    public void tick() {

        for(int i = 0 ; i<360;i=i+15) {
            double p;
            double o;
            double n;
            int m =  0xFF3300;
            n = (float)(m >> 16 & 0xFF) / 255.0f;
            o = (float)(m >> 8 & 0xFF) / 255.0f;
            p = (float)(m & 0xFF) / 255.0f;

            this.world.addImportantParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, getX()+Math.cos(i)*getRadius()/2,
                    getY(),
                    getZ()+Math.sin(i)*getRadius()/2,n,o,p);
        }
        LogManager.getLogger().info(this.getRadius());
        LogManager.getLogger().info(this.radiusGrowth);
        this.setRadius(this.getRadius() + this.getRadiusGrowth());
        if(this.getRadius()<0.5f){
            discard();
        }
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
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius() * 2.0f, 0.5f);
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
}
