package com.flylx.wand_mod.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MagicAreaCloud extends AreaEffectCloudEntity {

    private  static final TrackedData<Float> DEGREE = DataTracker.registerData(MagicAreaCloud.class,
            TrackedDataHandlerRegistry.FLOAT);
    private final Map<Entity, Integer> affectedEntities = Maps.newHashMap();
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
        if (this.owner == null && this.ownerUuid != null && this.world instanceof ServerWorld && (entity = ((ServerWorld) this.world).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (LivingEntity) entity;
        }
        return this.owner;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(RADIUS, Float.valueOf(0.5f));
        this.getDataTracker().startTracking(DEGREE, Float.valueOf(0.0f));

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

    public void setDegree(float degree) {
        if (!this.world.isClient) {
            this.getDataTracker().set(DEGREE, degree);
        }
    }

    public float getDegree() {
        return this.getDataTracker().get(DEGREE).floatValue();
    }

    @Override
    public void tick() {
        double p;
        double o;
        double n;
        if(this.getDegree()>=0&&this.getDegree()<60) {
//            if (this.getRadius() > 1.5) {
//                for (int i = 0; i < 360; i = i + 15) {
//                    int m = 0xFF3300;
//                    n = (float) (m >> 16 & 0xFF) / 255.0f;
//                    o = (float) (m >> 8 & 0xFF) / 255.0f;
//                    p = (float) (m & 0xFF) / 255.0f;
//
//                    this.world.addImportantParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT,
//                            getX() + Math.cos(i) * getRadius() / 1.5f,
//                            getY(),
//                            getZ() + Math.sin(i) * getRadius() / 1.5f, n, o, p);
//                }
//            } else {
//                int m = 0xFF3300;
//                for (int q = 0; q < 10; q++) {
//                    n = (float) (m >> 16 & 0xFF) / 255.0f;
//                    o = (float) (m >> 8 & 0xFF) / 255.0f;
//                    p = (float) (m & 0xFF) / 255.0f;
//                    this.world.addImportantParticle(ParticleTypes.ENTITY_EFFECT, getX(),
//                            getY() + q,
//                            getZ(), n, o, p);
//
//                }
//            }

            //fire

                double d;
                double e;
                double l;
                for (int i = 0; i < getRadius(); i++) {
                    n = (0.5 - this.random.nextDouble()) * 0.15;
                    o = 0.01f;
                    p = (0.5 - this.random.nextDouble()) * 0.15;
                    d = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    e = this.random.nextDouble()-this.random.nextDouble();

                    l = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    this.world.addImportantParticle(ParticleTypes.SMALL_FLAME,
                            getX() + d,
                            getY() + e,
                            getZ() + l, n, o, p);

            }
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
            float damage = this.getDegree()/10;

            for (LivingEntity livingEntity : list) {
                livingEntity.setFireTicks(200);
//                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE);
//                statusEffectInstance.getEffectType()
//                        .applyInstantEffect(this,this.getOwner(),livingEntity,statusEffectInstance.getAmplifier(),0.5);
                livingEntity.damage(DamageSource.GENERIC,damage);


            }
        }else  if(this.getDegree()>=60&&this.getDegree()<120) {
            //ice
                double d;
                double e;
                double l;
                for (int i = 0; i < getRadius(); i++) {
                    n = (0.5 - this.random.nextDouble()) * 0.15;
                    o = 0.01f;
                    p = (0.5 - this.random.nextDouble()) * 0.15;
                    d = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    e = this.random.nextDouble()-this.random.nextDouble();

                    l = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    this.world.addImportantParticle(ParticleTypes.SNOWFLAKE,
                            getX() + d,
                            getY() + e,
                            getZ() + l, n, o, p);

            }
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
            float damage = (this.getDegree()-60)/10;
            for (LivingEntity livingEntity : list) {
                livingEntity.setFrozenTicks(200);
//                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE);
//                statusEffectInstance.getEffectType()
//                        .applyInstantEffect(this,this.getOwner(),livingEntity,statusEffectInstance.getAmplifier(),damage);
                livingEntity.damage(DamageSource.GENERIC,damage);

            }


        }else  if(this.getDegree()>=120&&this.getDegree()<180) {

            //poison
                double d;
                double e;
                double l;
                int m = 0x00FF22;
                n = (float) (m >> 16 & 0xFF) / 255.0f;
                o = (float) (m >> 8 & 0xFF) / 255.0f;
                p = (float) (m & 0xFF) / 255.0f;
                for (int i = 0; i < getRadius(); i++) {

                    d = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    e = this.random.nextDouble()-this.random.nextDouble();

                    l = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                    this.world.addImportantParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT,
                            getX() + d,
                            getY() + e,
                            getZ() + l, n, o, p);

            }
            float damage = (this.getDegree()-120)/10;
                //给玩家效果
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
            for (LivingEntity livingEntity : list) {

                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.POISON);

                statusEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(),
                        200, (int) damage,
                        statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
                livingEntity.addStatusEffect(new StatusEffectInstance(statusEffectInstance),getOwner());
                livingEntity.damage(DamageSource.GENERIC,damage);
            }


        }else if(this.getDegree()>=180&&this.getDegree()<240) {

            //heart
            double d;
            double e;
            double l;
            for (int i = 0; i < getRadius(); i++) {
                n = (0.5 - this.random.nextDouble()) * 0.15;
                o = 0.01f;
                p = (0.5 - this.random.nextDouble()) * 0.15;
                d = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                e = this.random.nextDouble()-this.random.nextDouble();

                l = (this.random.nextDouble()-this.random.nextDouble())*getRadius();
                this.world.addImportantParticle(ParticleTypes.HEART,
                        getX() + d,
                        getY() + e,
                        getZ() + l, n, o, p);

            }
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
            float damage = (this.getDegree()-180)/100;

            for (LivingEntity livingEntity : list) {
                StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_HEALTH);
                statusEffectInstance.getEffectType()
                        .applyInstantEffect(this,this.getOwner(),livingEntity,statusEffectInstance.getAmplifier(),damage);

            }
        }

        //逐渐减少范围
        this.setRadius(this.getRadius() + this.getRadiusGrowth());
        if(this.getRadius()<0.5f){
            discard();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.radiusGrowth = nbt.getFloat("RadiusPerTick");
        this.setRadius(nbt.getFloat("Radius"));
        this.setDegree(nbt.getFloat("Degree"));
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
        nbt.putFloat("Degree", this.getDegree());
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
