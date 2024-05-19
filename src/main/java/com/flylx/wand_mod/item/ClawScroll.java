package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.BasicMagic;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ClawScroll extends Item implements  IAnimatable, ISyncable {

    public static final String controllerName = "claw_controller";
    public static final int ANIM_OPEN = 0;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private BasicMagic basicMagic;

    public ClawScroll(Settings settings) {
        super(settings);
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,controllerName,1,this::predicate));

    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_OPEN) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);

            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("open",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));


        }else{

        }
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {


//        if (!world.isClient()) {
//            remain_time = world.getTime()-now_time;
//            if(remain_time >60){
//                if(this.basicMagic!=null){
//                    this.basicMagic.discard();
//                    this.basicMagic = null;
//                }
//                return;
//            }
//            if(entity instanceof PlayerEntity) {
//
//                if(((PlayerEntity)entity).handSwinging&&this.basicMagic!=null){
//                    stack.decrement(1);
//                }
//                final int id = GeckoLibUtil.guaranteeIDForStack(((PlayerEntity)entity).getStackInHand(Hand.OFF_HAND), (ServerWorld) world);
//
//                final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
//                LogManager.getLogger().info("state:"+controller.getAnimationState());
//                if (((PlayerEntity)entity).getOffHandStack().isOf(modItemRegistry.CLAW_SCROLL)) {
//                    if(this.basicMagic == null) {
//
//
//                        basicMagic = new BasicMagic(world, ((PlayerEntity)entity));
//
//                        basicMagic.setVelocity(((PlayerEntity)entity), ((PlayerEntity)entity).getPitch(), ((PlayerEntity)entity).getYaw(), 0F, 1.0F,
//                                0F);
//
//                        basicMagic.age = 30;
//
//                        basicMagic.hasNoGravity();
//                        basicMagic.setDegree(270);
//                        basicMagic.setOwnerItem(stack);
////                        stack.decrement(1);
//                        world.spawnEntity(basicMagic);
//                    }
//
//                }
//            }
//        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(Hand.OFF_HAND);
        if (!world.isClient()) {
            if(user.getOffHandStack().isOf(modItemRegistry.CLAW_SCROLL)) {
                final int id = GeckoLibUtil.guaranteeIDForStack(user.getStackInHand(Hand.OFF_HAND), (ServerWorld) world);
                GeckoLibNetwork.syncAnimation(user, this, id, ANIM_OPEN);
                for (PlayerEntity otherPlayer : PlayerLookup.tracking(user)) {
                    GeckoLibNetwork.syncAnimation(otherPlayer, this, id, ANIM_OPEN);

                }

            }

        }

        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);

        if(world.isClient) {
            return;
        }
        NbtCompound nbtCompound = new NbtCompound();
        int i = stack.getMaxUseTime() - remainingUseTicks;

        LogManager.getLogger().info(i);
        if(i>10&&i<50){
        if(user instanceof ServerPlayerEntity) {

            if (!user.getOffHandStack().hasNbt()) {
                nbtCompound.putBoolean("content", true);
                user.getOffHandStack().setNbt(nbtCompound);
                spawnMagic(world, (ServerPlayerEntity)user, user.getOffHandStack());
            } else {
                if (!user.getOffHandStack().getNbt().getBoolean("content")) {

                    nbtCompound.putBoolean("content", true);
                    user.getOffHandStack().setNbt(nbtCompound);
                    spawnMagic(world, (ServerPlayerEntity)user, user.getOffHandStack());
                }
            }
            }
        }
    }

    public void spawnMagic(World world, ServerPlayerEntity entity, ItemStack stack){

        basicMagic = new BasicMagic(world, ((PlayerEntity)entity));

        basicMagic.setVelocity(((PlayerEntity)entity), ((PlayerEntity)entity).getPitch(), ((PlayerEntity)entity).getYaw(), 0F, 1.0F,
                0F);

        basicMagic.age = 30;

        basicMagic.hasNoGravity();
        basicMagic.setDegree(270);
        basicMagic.setOwnerItem(stack);
//                        stack.decrement(1);
        world.spawnEntity(basicMagic);
    }


}
