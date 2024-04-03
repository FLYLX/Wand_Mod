package com.flylx.wand_mod.mob;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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


    public MagicGolemEntity(EntityType<? extends IronGolemEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMagicGolemEntity() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
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

    @Nullable
    @Override
    public UUID getAngryAt() {
        return null;
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

    }

    @Override
    public void tick() {
        super.tick();
        if(this.getWorld().getTime()%10 == 0) {
            this.getWorld().addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
                    this.getX() + random.nextFloat() * 2 -1,
                    this.getY() + random.nextFloat() * 2.5f, this.getZ() + random.nextFloat() * 2 -1, 0, 0, 0);
        }
    }
    @Override
    public void chooseRandomAngerTime() {

    }

}
