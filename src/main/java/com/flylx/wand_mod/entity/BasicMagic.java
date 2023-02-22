package com.flylx.wand_mod.entity;

import com.flylx.wand_mod.item.animated_base_wand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.LogManager;
import software.bernie.example.entity.RocketProjectile;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.geckolib3.core.AnimationState;
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

import java.util.Iterator;
import java.util.List;

public class BasicMagic extends PersistentProjectileEntity implements IAnimatable, ISyncable {
    AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private LivingEntity shooter;

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

        this.world.addParticle(ParticleTypes.FIREWORK, this.getParticleX(0.5D), this.getRandomBodyY(),
                this.getParticleZ(0.5D),this.getVelocity().x,this.getVelocity().y ,this.getVelocity().z );

        if(controller.getAnimationState()== AnimationState.Stopped){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("spin",ILoopType.EDefaultLoopTypes.LOOP));
        }

        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        if (!(target instanceof PlayerEntity)) {
            target.setVelocity(0, 0, 0);
            target.timeUntilRegen = 0;
        }
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient) {
            this.doDamage();
            this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 0.0F,
                    Explosion.DestructionType.BREAK);
            this.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            this.doDamage();
            this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 0.0F,
                    Explosion.DestructionType.BREAK);
            this.remove(Entity.RemovalReason.DISCARDED);
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
        float q = 4.0F;
        int k = MathHelper.floor(this.getX() - (double) q - 1.0D);
        int l = MathHelper.floor(this.getX() + (double) q + 1.0D);
        int t = MathHelper.floor(this.getY() - (double) q - 1.0D);
        int u = MathHelper.floor(this.getY() + (double) q + 1.0D);
        int v = MathHelper.floor(this.getZ() - (double) q - 1.0D);
        int w = MathHelper.floor(this.getZ() + (double) q + 1.0D);
        List<Entity> list = this.world.getOtherEntities(this,
                new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
        Vec3d vec3d = new Vec3d(this.getX(), this.getY(), this.getZ());
        for (int x = 0; x < list.size(); ++x) {
            Entity entity = (Entity) list.get(x);
            double y = (double) (MathHelper.sqrt((float) entity.squaredDistanceTo(vec3d)) / q);
            if (y <= 1.0D) {
                if (entity instanceof LivingEntity) {
                    entity.damage(DamageSource.player((PlayerEntity) this.shooter), 20);
                }
                this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 0.0F,
                        Explosion.DestructionType.NONE);
            }
        }
    }




}
