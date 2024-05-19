package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.BasicMagic;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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

public class FrozeScroll extends Item implements IAnimatable, ISyncable {

    public static final String controllerName = "controller2";
    public static final int ANIM_OPEN = 1;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public long now_time,remain_time;

    public FrozeScroll(Settings settings) {
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
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        if (state == ANIM_OPEN) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("open",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
    }




    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {

            remain_time = world.getTime()-now_time;
            if(remain_time >30){
                return;
            }

            if (entity instanceof PlayerEntity) {
                if (((PlayerEntity) entity).getOffHandStack().isOf(modItemRegistry.FROZE_SCROLL) &&
                        ((PlayerEntity) entity).handSwinging) {

                    BasicMagic basicMagic = new BasicMagic(world, ((PlayerEntity)entity));

                    basicMagic.setVelocity(((PlayerEntity)entity), ((PlayerEntity)entity).getPitch(), ((PlayerEntity)entity).getYaw(), 0F, 1.0F,
                            0F);


                    basicMagic.age = 30;

                    basicMagic.hasNoGravity();
                    basicMagic.setDegree(90);
                    ((PlayerEntity) entity).getInventory().setStack(PlayerInventory.OFF_HAND_SLOT,ItemStack.EMPTY);
                    world.spawnEntity(basicMagic);

                }
            }
        }


        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if(user.getOffHandStack().isOf(modItemRegistry.FROZE_SCROLL)) {

                user.setCurrentHand(Hand.OFF_HAND);
                final int id = GeckoLibUtil.guaranteeIDForStack(user.getStackInHand(Hand.OFF_HAND), (ServerWorld) world);
                GeckoLibNetwork.syncAnimation(user, this, id, ANIM_OPEN);
                for (PlayerEntity otherPlayer : PlayerLookup.tracking(user)) {

                    GeckoLibNetwork.syncAnimation(otherPlayer, this, id, ANIM_OPEN);
                }
                now_time = world.getTime();

            }

        }

        return TypedActionResult.consume(itemStack);
    }
}
