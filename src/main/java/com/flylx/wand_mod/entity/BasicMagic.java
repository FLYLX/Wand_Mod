package com.flylx.wand_mod.entity;

import com.flylx.wand_mod.event.SwitchMagic;
import com.flylx.wand_mod.util.IEntityDataSaver;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
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

import java.util.List;

public class BasicMagic extends PersistentProjectileEntity implements IAnimatable, ISyncable {
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
        if(degree >360.0F) {
            degree = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getFloat("switch");
        }
        this.world.addParticle(ParticleTypes.FIREWORK, this.getParticleX(0.5D), this.getRandomBodyY(),
                this.getParticleZ(0.5D),this.getVelocity().x,this.getVelocity().y ,this.getVelocity().z );

        if(controller.getAnimationState()== AnimationState.Stopped){
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("spin",ILoopType.EDefaultLoopTypes.LOOP));
        }
        LogManager.getLogger().info("tick里的degree:"+degree);

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
            this.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            this.doDamage();
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
        SwitchMagic switchMagic = new SwitchMagic(this,this.shooter);
        switchMagic.theMagic();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("magicType", degree);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        degree = nbt.getFloat("magicType");
        LogManager.getLogger().info("nbt里的degree:"+nbt.getFloat("magicType"));
        super.readNbt(nbt);
    }
}
