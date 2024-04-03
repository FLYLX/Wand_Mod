package com.flylx.wand_mod.entity;



import com.flylx.wand_mod.util.IEntityDataSaver;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;


import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;


import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.LogManager;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;



public class BasicMagic extends PersistentProjectileEntity implements IAnimatable, ISyncable {

    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private LivingEntity shooter;
    public int basicmagicAge;

    public static final int ANIM_OPEN = 1;
    public static final String controllerName = "controller";
    AnimationController<BasicMagic> controller = new AnimationController(this,controllerName , 1,this::predicate);
    private static final TrackedData<Float> DEGREE = DataTracker.registerData(BasicMagic.class,
            TrackedDataHandlerRegistry.FLOAT);

    private float Degree;

    public BasicMagic(EntityType<? extends BasicMagic> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
        this.basicmagicAge = this.random.nextInt(100000);
    }

    //还没写完，参考鱼钩实体类


    public BasicMagic(World world,LivingEntity owner){
        super(modEntityRegistry.BASIC_MAGIC, owner, world);
        this.shooter = owner;
        Degree = ((IEntityDataSaver) owner).getPersistentData().getFloat(
                "switch");
        this.getDataTracker().set(DEGREE,Degree);

        this.setOwner(owner);
        float f = owner.getPitch();
        float g = owner.getYaw();
        float h = MathHelper.cos(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float i = MathHelper.sin(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float k = MathHelper.sin(-f * ((float)Math.PI / 180));
        double d = owner.getX() - (double)i * 0.3;
        double e = owner.getEyeY();
        double l = owner.getZ() - (double)h * 0.3;
        this.refreshPositionAndAngles(d, e, l, g, f);
        Vec3d vec3d = new Vec3d(-i, MathHelper.clamp(-(k / j), -5.0f, 5.0f), -h);
        double m = vec3d.length();
        vec3d = vec3d.multiply(0.6 / m + this.random.nextTriangular(0.5, 0.0103365), 0.6 / m + this.random.nextTriangular(0.5, 0.0103365), 0.6 / m + this.random.nextTriangular(0.5, 0.0103365));
        this.setVelocity(vec3d);
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DEGREE, Degree);
    }



    public void setDegree(float degree) {
        if (!this.world.isClient) {
            this.getDataTracker().set(DEGREE, Float.valueOf(degree));
        }
    }

    public float getDegree() {
        return this.getDataTracker().get(DEGREE).floatValue();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.AIR);
    }

    @Override
    public boolean cannotBeSilenced() {
        return super.cannotBeSilenced();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(controller);
    }

    @Override
    public void tick() {
        ++basicmagicAge;
        LogManager.getLogger().info(getDegree());
        if(Math.sqrt(Math.pow(this.getVelocity().x,2)+Math.pow(this.getVelocity().y,2)+Math.pow(this.getVelocity().z,2))<0.3F) {
            this.discard();
        }

        if(getDegree()>=240&&getDegree()<300){
            hookPlayer((PlayerEntity) getOwner(),this.getWorld(),this);

        }

        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.world.isClient) {
            return;
        }
        this.discard();
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.world.isClient) {
            return;
        }

    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (this.world.isClient) {
            return;
        }
        doDamage();
        this.discard();
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_OPEN) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("magic",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
    }

    public void doDamage() {
        if(getDegree()>=0&&getDegree()<60) {
            explosionMagic();


        }else if (getDegree()>=60&&getDegree()<120){
            frozeMagic();

        }else if(getDegree()>=120&&getDegree()<180){
            poisonMagic();
        }if(getDegree()>=180&&getDegree()<240){
            heartMagic();
        }


    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        LogManager.getLogger().info("target:"+target);
        if(getDegree()>=0&&getDegree()<60) {
            //fire
            target.setFireTicks(2000);
            target.damage(DamageSource.ON_FIRE,5);
        }else if (getDegree()>=60&&getDegree()<120){
            //ice
            target.setFrozenTicks(2000);
            target.damage(DamageSource.FREEZE,5);
        }else if(getDegree()>=120&&getDegree()<180){
            //poison
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.POISON);

            statusEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(),
                    2000, 20,
                    statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
            target.addStatusEffect(new StatusEffectInstance(statusEffectInstance),getOwner());

        }else if(getDegree()>=180&&getDegree()<240){
            //heart
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_HEALTH);

            statusEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(),
                    2000, 20,
                    statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
            target.addStatusEffect(new StatusEffectInstance(statusEffectInstance),getOwner());

        }else if(getDegree()>=240&&getDegree()<300){
            target.setVelocity((this.getOwner().getX()-target.getX())/10,(this.getOwner().getY()-target.getY())/10,
                    (this.getOwner().getZ()-target.getZ())/10);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("Degree", this.getDegree());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.setDegree(nbt.getFloat("Degree"));

        super.readNbt(nbt);
    }


//magic type
    public void explosionMagic(){
        MagicAreaCloud magicAreaCloud = new MagicAreaCloud(this.world,this.getX(),this.getY(),this.getZ());
        magicAreaCloud.setRadius(6.0f);
        magicAreaCloud.setRadiusGrowth(-0.05f);
        magicAreaCloud.setDegree(getDegree());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            magicAreaCloud.setOwner((LivingEntity) entity);
        }
        this.world.spawnEntity(magicAreaCloud);
        LogManager.getLogger().info("done");
        if(this.world.getBlockState(this.getBlockPos()).equals(Blocks.AIR.getDefaultState())) {
            this.world.setBlockState(new BlockPos(this.getPos()), Blocks.FIRE.getDefaultState());
        }
    }

    @Override
    public boolean isSubmergedInWater() {
        return true;
    }

    public void frozeMagic() {
        MagicAreaCloud magicAreaCloud = new MagicAreaCloud(this.world,this.getX(),this.getY(),this.getZ());
        magicAreaCloud.setRadius(6.0f);
        magicAreaCloud.setRadiusGrowth(-0.05f);
        magicAreaCloud.setDegree(getDegree());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            magicAreaCloud.setOwner((LivingEntity) entity);
        }
        this.world.spawnEntity(magicAreaCloud);
        if(this.world.getBlockState(this.getBlockPos()).equals(Blocks.AIR.getDefaultState())) {

        }
    }

    public void poisonMagic(){
        MagicAreaCloud magicAreaCloud = new MagicAreaCloud(this.world,this.getX(),this.getY(),this.getZ());
        magicAreaCloud.setRadius(6.0f);
        magicAreaCloud.setRadiusGrowth(-0.05f);
        magicAreaCloud.setDegree(getDegree());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            magicAreaCloud.setOwner((LivingEntity) entity);
        }
        this.world.spawnEntity(magicAreaCloud);

    }
    public void heartMagic(){
        MagicAreaCloud magicAreaCloud = new MagicAreaCloud(this.world,this.getX(),this.getY(),this.getZ());
        magicAreaCloud.setRadius(6.0f);
        magicAreaCloud.setRadiusGrowth(-0.05f);
        magicAreaCloud.setDegree(getDegree());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            magicAreaCloud.setOwner((LivingEntity) entity);
        }
        this.world.spawnEntity(magicAreaCloud);

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }



    public void hookPlayer(PlayerEntity player, World world, Entity entity){
        if(getOwner()!=null) {
            if (((PlayerEntity) this.getOwner()).handSwinging) {
                player.setVelocity(player.getVelocity().getX() + (entity.getPos().getX() - player.getPos().getX())/2 ,
                        player.getVelocity().getY() + (entity.getPos().getY() - player.getPos().getY()) /5,
                        player.getVelocity().getZ() + (entity.getPos().getZ() - player.getPos().getZ())/2

                );
                this.discard();
            }
        }
    }

    @Override
    public float getYaw() {
        return super.getYaw();
    }
}
