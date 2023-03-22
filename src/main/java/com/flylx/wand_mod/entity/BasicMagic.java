package com.flylx.wand_mod.entity;


import com.flylx.wand_mod.util.IEntityDataSaver;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;


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

import java.util.List;


public class BasicMagic extends PersistentProjectileEntity implements IAnimatable, ISyncable {

    public volatile boolean isExist = true;
    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private LivingEntity shooter;
    public float degree = 1000.0F;
    public static final int ANIM_OPEN = 1;
    public static final String controllerName = "controller";
    AnimationController<BasicMagic> controller = new AnimationController(this,controllerName , 1,this::predicate);

    public BasicMagic(EntityType<? extends BasicMagic> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public BasicMagic(World world,LivingEntity owner){
        super(modEntityRegistry.BASIC_MAGIC, owner, world);
        this.shooter = owner;


    }

    public BasicMagic(EntityType<? extends BasicMagic> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public BasicMagic(EntityType<? extends BasicMagic> type, LivingEntity owner, World world) {
        super(type, owner, world);
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        return PlayState.CONTINUE;
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
        LogManager.getLogger().info(isExist);
        LogManager.getLogger().info(this.getVelocity().x+"   "+this.getVelocity().y+"   "+this.getVelocity().z);
        if(degree >360.0F) {
            degree = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getFloat("switch");
        }

        if(Math.sqrt(Math.pow(this.getVelocity().x,2)+Math.pow(this.getVelocity().y,2)+Math.pow(this.getVelocity().z
                ,2))<0.3F) {
            this.discard();
        }

        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        LogManager.getLogger().info("entity hit");
        if (!this.world.isClient) {
            doDamage();
            SleepAndDie(100);
            if(entityHitResult.getEntity() instanceof LivingEntity) {
                onHit((LivingEntity) entityHitResult.getEntity());
            }
//            this.remove(Entity.RemovalReason.DISCARDED);
        }


    }


    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            isExist = false;
            this.onEntityHit((EntityHitResult)hitResult);
            this.world.emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
//            BlockHitResult blockHitResult = new BlockHitResult(hitResult.getPos(),null,
//                    new BlockPos(hitResult.getPos()),true);
//            this.onBlockHit(blockHitResult);
//            this.world.emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this,
//                    this.world.getBlockState(new BlockPos(hitResult.getPos()))));
        } else if (type == HitResult.Type.BLOCK) {
            isExist = false;
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
            this.world.emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.world.getBlockState(blockPos)));
        }

    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

        LogManager.getLogger().info("block hit");
        if (!this.world.isClient) {

              doDamage();
              SleepAndDie(100);
//            this.remove(Entity.RemovalReason.DISCARDED);
        }

        this.setSound(SoundEvents.ENTITY_GENERIC_EXPLODE);

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
        if(degree>=0&&degree<60) {
            LogManager.getLogger().info("explosion");
            explosionMagic();

        }else if (degree>=60&&degree<120){
            frozeMagic();

        }


    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        LogManager.getLogger().info("target:"+target);
        if(degree>=0&&degree<60) {
            target.setFireTicks(2000);
            target.damage(DamageSource.ON_FIRE,5);
        }else if (degree>=60&&degree<120){
            target.setFrozenTicks(2000);
            target.damage(DamageSource.FREEZE,5);
        }
    }

    public void explosionMagic(){
        this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 0.0F,
                Explosion.DestructionType.NONE);
        this.world.setBlockState(new BlockPos(this.getPos()), Blocks.FIRE.getDefaultState());

    }


    public void frozeMagic() {
        this.world.setBlockState(new BlockPos(this.getPos()), Blocks.WATER.getDefaultState());

    }




    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("magicType", degree);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        degree = nbt.getFloat("magicType");
        super.readNbt(nbt);
    }

    public void SleepAndDie(int time){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                    discard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

}
