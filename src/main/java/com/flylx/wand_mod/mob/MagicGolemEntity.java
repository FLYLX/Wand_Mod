package com.flylx.wand_mod.mob;

import com.flylx.wand_mod.entity.BasicMagic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MagicGolemEntity extends IronGolemEntity implements Angerable {

    private MagicGolemTypes magicGolemTypes;
    private static final TrackedData<String> GOLEM_TYPES = DataTracker.registerData(MagicGolemEntity.class,
            TrackedDataHandlerRegistry.STRING);

    public MagicGolemEntity(EntityType<? extends MagicGolemEntity> entityType, World world) {
        super(entityType, world);
    }


    public static DefaultAttributeContainer.Builder createMagicGolemEntity() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
    }

    public MagicGolemTypes getMagicGolemTypes() {
        return MagicGolemType.StringToMagicType(this.getDataTracker().get(GOLEM_TYPES));
    }
    public void setMagicGolemTypes(MagicGolemTypes magicGolemTypes) {
        if (!this.world.isClient) {
            this.magicGolemTypes = magicGolemTypes;
            this.getDataTracker().set(GOLEM_TYPES, MagicGolemType.MagicTypeToString(magicGolemTypes));
        }

    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(GOLEM_TYPES, "fire");

    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("golem_types", MagicGolemType.MagicTypeToString(getMagicGolemTypes()));
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.setMagicGolemTypes(MagicGolemType.StringToMagicType(nbt.getString("golem_types")));
        super.readNbt(nbt);
    }

    @Override
    protected int getNextAirUnderwater(int air) {
        return super.getNextAirUnderwater(air);
    }

    @Override
    protected void pushAway(Entity entity) {
        super.pushAway(entity);
    }

    @Override
    public void tickMovement() {

        super.tickMovement();
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        return super.canTarget(type);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }


    @Override
    protected void initGoals() {
        super.initGoals();
    }

    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {

    }

    @Override
    public double squaredAttackRange(LivingEntity target) {
        return this.getWidth() * 2.0f * (this.getWidth() * 2.0f) + target.getWidth()+20.0f;
    }



    public void summonBasicMagic(Entity target,MagicGolemTypes magicGolemTypes){

        BasicMagic basicMagic = new BasicMagic(this.world,this);
        basicMagic.setVelocity((target.getX()-this.getX())/3,(target.getY()+1-this.getY())/3,
                (target.getZ()-this.getZ())/3);
        basicMagic.setPosition(this.getX(),this.getY()+1,this.getZ());
        basicMagic.age = 30;
        basicMagic.hasNoGravity();
        basicMagic.setOwner(this);
        switch(magicGolemTypes){
            case FIRE :
                basicMagic.setDegree(15);
                this.world.spawnEntity(basicMagic);
                break;
            case FROZE:
                basicMagic.setDegree(75);
                this.world.spawnEntity(basicMagic);
                break;
            case POISON:
                basicMagic.setDegree(135);
                this.world.spawnEntity(basicMagic);
                break;
            case END:
                basicMagic.setDegree(255);
                this.world.spawnEntity(basicMagic);
                break;
            case STONE:
                basicMagic.setDegree(315);
                this.world.spawnEntity(basicMagic);
                break;
        }
    }
    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return super.damage(source, amount);
    }

    @Override
    public Crack getCrack() {
        return super.getCrack();
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
    }

    @Override
    public int getAttackTicksLeft() {
        return super.getAttackTicksLeft();
    }

    @Override
    public void setLookingAtVillager(boolean lookingAtVillager) {
        super.setLookingAtVillager(lookingAtVillager);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
    }

    @Override
    public int getLookingAtVillagerTicks() {
        return super.getLookingAtVillagerTicks();
    }

    @Override
    public boolean isPlayerCreated() {
        return super.isPlayerCreated();
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        super.setPlayerCreated(playerCreated);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);

    }

    @Override
    public boolean canSpawn(WorldView world) {
        return super.canSpawn(world);
    }

    @Override
    public Vec3d getLeashOffset() {
        return super.getLeashOffset();
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        super.setAngryAt(angryAt);
    }

    public void magicParticle(MagicGolemTypes magicGolemTypes){
        switch(magicGolemTypes){
            case FIRE :
                this.getWorld().addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
                        this.getX() + random.nextFloat() * 2 -1,
                        this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1, 0, 0, 0);
                break;
            case FROZE:
                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE,
                        this.getX() + random.nextFloat() * 2 -1,
                        this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1, 0, 0, 0);

                break;
            case POISON:
                int m = 0x00FF22;
                double n = (float) (m >> 16 & 0xFF) / 255.0f;
                double o = (float) (m >> 8 & 0xFF) / 255.0f;
                double p = (float) (m & 0xFF) / 255.0f;
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, true,this.getX() + random.nextFloat() * 2 -1,
                        this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1,
                        n,o,p );

                break;
            case END:
                this.getWorld().addParticle(ParticleTypes.END_ROD,
                        this.getX() + random.nextFloat() * 2 -1,
                        this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1, 0, 0, 0);
                break;
            case STONE:
                this.getWorld().addParticle(ParticleTypes.ASH,
                        this.getX() + random.nextFloat() * 2 -1,
                        this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1, 0, 0, 0);
                break;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.world.isClient){
            if(this.getWorld().getTime()%10 == 0) {
                magicParticle(this.getMagicGolemTypes());

            }
        }
        if(!this.world.isClient){
            if(this.getTarget()!=null&&random.nextFloat() < 0.03f) {
                summonBasicMagic(this.getTarget(), this.getMagicGolemTypes());
            }
        }
    }
    @Override
    public void chooseRandomAngerTime() {
        super.chooseRandomAngerTime();
    }

}
