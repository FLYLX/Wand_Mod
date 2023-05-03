package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.event.LeftClick;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PoisonScroll extends Item implements IAnimatable, ISyncable {

    public static final String controllerName = "controller";
    public static final int ANIM_OPEN = 1;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public PoisonScroll(Settings settings) {
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
            final int id = GeckoLibUtil.getIDFromStack(stack);
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            if(MinecraftClient.getInstance().player!=null&&
                    MinecraftClient.getInstance().player.getOffHandStack().isOf(modItemRegistry.POISON_SCROLL)&&
                    controller.getAnimationState()== AnimationState.Running&& LeftClick.isClick) {
                PlayerEntity playerentity = (PlayerEntity) MinecraftClient.getInstance().player;

                BasicMagic basicMagic = new BasicMagic(world, playerentity);

                basicMagic.setVelocity(playerentity, playerentity.getPitch(), playerentity.getYaw(), 0F, 1.0F,
                        0F);

                basicMagic.age = 30;

                basicMagic.hasNoGravity();
                basicMagic.setDegree(150);
                stack.decrement(1);
                world.spawnEntity(basicMagic);

            }
        }


        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if(user.getOffHandStack().isOf(modItemRegistry.POISON_SCROLL)) {

                user.setCurrentHand(Hand.OFF_HAND);
                final int id = GeckoLibUtil.guaranteeIDForStack(user.getStackInHand(Hand.OFF_HAND), (ServerWorld) world);
                GeckoLibNetwork.syncAnimation(user, this, id, ANIM_OPEN);
                for (PlayerEntity otherPlayer : PlayerLookup.tracking(user)) {

                    GeckoLibNetwork.syncAnimation(otherPlayer, this, id, ANIM_OPEN);
                }

            }

        }

        return TypedActionResult.consume(itemStack);
    }
}
