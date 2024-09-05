package com.flylx.wand_mod.mob;

import com.flylx.wand_mod.entity.BasicMagic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
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

import java.util.Random;
import java.util.function.Predicate;

public class MagicPolymer extends HostileEntity implements RangedAttackMob,IAnimatable , ISyncable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String controllerName = "magic_polymer";
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = entity -> entity.getGroup() != EntityGroup.UNDEAD && entity.isMobOrPlayer();

    protected MagicPolymer(EntityType<? extends MagicPolymer> entityType, World world) {
        super(entityType, world);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.age <10){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("spawn", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("loop", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }



    @Nullable
    @Override
    public EntityAttributeInstance getAttributeInstance(EntityAttribute attribute) {
        return super.getAttributeInstance(attribute);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<MagicPolymer>(this,controllerName,
                0,this::predicate));
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public static DefaultAttributeContainer.Builder createMagicPolymerEntity() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);

    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0, 40, 20.0f));
        this.goalSelector.add(5, new FlyGoal(this, 10.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 20.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<LivingEntity>(this, LivingEntity.class, 0, false, false, CAN_ATTACK_PREDICATE));
    }

    @Override
    public void onAnimationSync(int id, int state) {

    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.shootSkullAt(0, target);
    }
    private void shootSkullAt(int headIndex, LivingEntity target) {
        this.shootMagicAt(target);
    }

    private void shootMagicAt( Entity target) {
        if (!this.isSilent()) {
            this.world.syncWorldEvent(null, WorldEvents.WITHER_SHOOTS, this.getBlockPos(), 0);
        }
        int choice = random.nextInt(6);
        if(choice > 4){
            double randx = (Math.random() * 2) - 1;
            double randz = (Math.random() * 2) - 1;
            BlockPos pos = new BlockPos(target.getX(),target.getY()+5,target.getZ());
            this.setPos(target.getX()+randx,target.getY(),target.getZ()+randz);
            BlockState dripstoneState = Blocks.POINTED_DRIPSTONE.getDefaultState().with(Properties.VERTICAL_DIRECTION, Direction.DOWN);
            spawnFallingBlock(dripstoneState, (ServerWorld) world, pos);

        }else if(choice < 2){
            BasicMagic basicMagic = new BasicMagic(this.world, this);
            basicMagic.setVelocity((target.getX() - this.getX()) / 3, (target.getY() + 1 - this.getY()) / 3,
                    (target.getZ() - this.getZ()) / 3);
            basicMagic.setPosition(this.getX(), this.getY() + 1, this.getZ());
            basicMagic.age = 30;
            basicMagic.hasNoGravity();
            basicMagic.setOwner(this);
            Random random = new Random();
            int randomNumber;

            if (choice == 0) {
                randomNumber = random.nextInt(180);  // 0 到 180 的随机数
            } else {
                randomNumber = 240 + random.nextInt(120);  // 240 到 360 的随机数
            }
            basicMagic.setDegree(randomNumber);
            this.world.spawnEntity(basicMagic);
        }else{
            LivingEntity targetEntity = world.getClosestEntity(LivingEntity.class, TargetPredicate.createNonAttackable().setBaseMaxDistance(6.0), this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expand(20.0, 6.0, 20.0));
            if(targetEntity!=null) {
                for (int i = 0;i<10;i++){
                    double xoffset = new Random().nextDouble() * 2 - 1;
                    double yoffset = new Random().nextDouble() * 2 - 1;
                    double zoffset = new Random().nextDouble() * 2 - 1;
                    world.addParticle(ParticleTypes.SCULK_SOUL,this.getX()+xoffset,this.getY()+yoffset+1,this.getZ()+zoffset,0,0,0);
                }
                targetEntity.setVelocity(0.0d,1.0d,0.0d);
                targetEntity.setFrozenTicks(200);
                world.createExplosion(targetEntity, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), 1.0f, true, Explosion.DestructionType.NONE);
            }
        }
    }
    private static void spawnFallingBlock(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        BlockState blockState = state;
        while (isPointingDown(blockState)) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, mutable, blockState);
            fallingBlockEntity.dropItem = false;
            if (isTip(blockState, true)) {
                int i = Math.max(1 + pos.getY() - mutable.getY(), 6);
                float f = 1.0f * (float)i;
                fallingBlockEntity.setHurtEntities(f, 40);
                break;
            }
            mutable.move(Direction.DOWN);
            blockState = world.getBlockState(mutable);
        }
    }

    private static boolean isPointingDown(BlockState state) {
        return isPointedDripstoneFacingDirection(state, Direction.DOWN);
    }

    private static boolean isPointedDripstoneFacingDirection(BlockState state, Direction direction) {
        return state.isOf(Blocks.POINTED_DRIPSTONE) && state.get(Properties.VERTICAL_DIRECTION) == direction;
    }


    private static boolean isTip(BlockState state, boolean allowMerged) {
        if (!state.isOf(Blocks.POINTED_DRIPSTONE)) {
            return false;
        }
        Thickness thickness = state.get(Properties.THICKNESS);
        return thickness == Thickness.TIP || allowMerged && thickness == Thickness.TIP_MERGE;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.BLOCKS;
    }
}
