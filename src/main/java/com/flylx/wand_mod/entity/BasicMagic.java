package com.flylx.wand_mod.entity;


import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.mob.MagicGolemEntity;
import com.flylx.wand_mod.mob.MagicGolemTypes;
import com.flylx.wand_mod.mob.modMobRegistry;
import com.flylx.wand_mod.sound.ModSounds;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
    private ItemStack ownerItem;

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
        if(owner instanceof PlayerEntity) {
            Degree = ((IEntityDataSaver) owner).getPersistentData().getFloat(
                    "switch");
        }else {
            Degree = 0;
        }
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
        //sounds

    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DEGREE, Degree);
    }

    public void setOwnerItem(ItemStack ownerItem) {
        this.ownerItem = ownerItem;
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
        if(basicmagicAge == 5){
            if(getDegree()>=0&&getDegree()<60) {
                world.playSound(null, getBlockPos(), ModSounds.FIRE_MAGIC, SoundCategory.BLOCKS, 1f, 1f);
            }else if (getDegree()>=60&&getDegree()<120){
                world.playSound(null, getBlockPos(), ModSounds.FROZE_MAGIC, SoundCategory.BLOCKS, 1f, 1f);
            }else if(getDegree()>=120&&getDegree()<180){
                world.playSound(null, getBlockPos(), ModSounds.POISON_MAGIC, SoundCategory.BLOCKS, 1f, 1f);
            }if(getDegree()>=180&&getDegree()<240){
                world.playSound(null, getBlockPos(), ModSounds.CURE_MAGIC, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        if(Math.sqrt(Math.pow(this.getVelocity().x,2)+Math.pow(this.getVelocity().y,2)+Math.pow(this.getVelocity().z,2))<0.3F) {
            if(ownerItem!=null) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putBoolean("content",false);
                ownerItem.setNbt(nbtCompound);
            }
            this.discard();
        }

        if(getDegree()>=240&&getDegree()<300){
            if(getOwner() instanceof  PlayerEntity) {
                hookPlayer((PlayerEntity) getOwner(), this.getWorld(), this);
            }
        }

        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void setDamage(double damage) {
        super.setDamage(0.0d);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.world.isClient) {
            return;
        }
        if(ownerItem!=null) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putBoolean("content",false);
            ownerItem.setNbt(nbtCompound);
        }
        this.discard();
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.world.isClient) {
            return;
        }
//        if(ownerItem!=null) {
//            NbtCompound nbtCompound = new NbtCompound();
//            nbtCompound.putBoolean("content",false);
//            ownerItem.setNbt(nbtCompound);
//        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return ModSounds.MAGIC_HIT;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (this.world.isClient) {
            return;
        }
        doDamage();
        if(ownerItem!=null) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putBoolean("content",false);
            ownerItem.setNbt(nbtCompound);
        }
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
        }else if(getDegree()>=180&&getDegree()<240){
            heartMagic();
        }else if(getDegree()>=300&&getDegree()<360){
            stoneMagic();
        }
    }



    public void change_golem(LivingEntity target,MagicGolemTypes magicGolemTypes){
        if(target instanceof IronGolemEntity&&!(target instanceof MagicGolemEntity)){
            MagicGolemEntity magicGolemEntity = modMobRegistry.MAGIC_GOLEM_ENTITY.create(world);
            magicGolemEntity.setMagicGolemTypes(magicGolemTypes);
            magicGolemEntity.setHealth(target.getHealth());
            magicGolemEntity.setPosition(target.getPos());
            world.spawnEntity(magicGolemEntity);
            target.discard();
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        if(getDegree()>=0&&getDegree()<60) {
            //fire
            target.setFireTicks(2000);
            target.damage(DamageSource.ON_FIRE,5);
            change_golem(target,MagicGolemTypes.FIRE);

        }else if (getDegree()>=60&&getDegree()<120){
            //ice
            target.setFrozenTicks(2000);
            target.damage(DamageSource.FREEZE,5);
            change_golem(target,MagicGolemTypes.FROZE);

        }else if(getDegree()>=120&&getDegree()<180){
            //poison
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.POISON);

            statusEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(),
                    2000, 20,
                    statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
            target.addStatusEffect(new StatusEffectInstance(statusEffectInstance),getOwner());
            change_golem(target,MagicGolemTypes.POISON);
        }else if(getDegree()>=180&&getDegree()<240){
            //heart
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_HEALTH);

            statusEffectInstance = new StatusEffectInstance(statusEffectInstance.getEffectType(),
                    8, 20,
                    statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
            target.addStatusEffect(new StatusEffectInstance(statusEffectInstance),getOwner());

        }else if(getDegree()>=240&&getDegree()<300){
            if(!world.isClient) {
                if (target instanceof ServerPlayerEntity) {
                    Vec3d vec3d = new Vec3d((this.getOwner().getX() - target.getX()) / 3,
                            (this.getOwner().getY() - target.getY()) / 3,
                            (this.getOwner().getZ() - target.getZ()) / 3);
                    ((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target.getId(), vec3d));
                } else {
                    target.setVelocity((this.getOwner().getX() - target.getX()) / 3,
                            (this.getOwner().getY() - target.getY()) / 3,
                            (this.getOwner().getZ() - target.getZ()) / 3);
                }

                change_golem(target,MagicGolemTypes.END);
                if((getOwner() instanceof  PlayerEntity)&&ownerItem.isOf(modItemRegistry.CLAW_SCROLL)){
                    ownerItem.decrement(1);
                }
            }
        }if(getDegree()>=300&&getDegree()<360){
            if(!(target instanceof IronGolemEntity)) {
                stoneMagic();
            }
            change_golem(target,MagicGolemTypes.STONE);
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

    public void stoneMagic(){
        MagicAreaCloud magicAreaCloud = new MagicAreaCloud(this.world,this.getX(),this.getY(),this.getZ());
        magicAreaCloud.setRadius(12.0f);
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
                player.setVelocity(player.getVelocity().getX() + (entity.getPos().getX() - player.getPos().getX())/3 ,
                        0 + (entity.getPos().getY() - player.getPos().getY()) /6,
                        player.getVelocity().getZ() + (entity.getPos().getZ() - player.getPos().getZ())/3
                );
                if(ownerItem != null && ownerItem.isOf(modItemRegistry.CLAW_SCROLL)){
                    ownerItem.decrement(1);
                }

                this.discard();
            }
        }
    }

    @Override
    public float getYaw() {
        return super.getYaw();
    }
}
