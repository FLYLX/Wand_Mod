package com.flylx.wand_mod.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
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

import java.util.Random;

public class WandBox extends Item implements IAnimatable, ISyncable {

    public static final String controllerName = "wand_box";
    public static final int ANIM_OPEN = 1;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public long MAX = Long.MAX_VALUE;


    public WandBox(Settings settings) {
        super(settings.maxDamage(201));
        GeckoLibNetwork.registerSyncable(this);
    }


    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }


    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
//        if (user instanceof PlayerEntity) {
//            PlayerEntity playerentity = (PlayerEntity) user;
//            if (!world.isClient()) {
//                playerentity.getItemCooldownManager().set(this,50);
//            }
//        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if(!world.isClient){
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 9.0f) {
                if(user instanceof  PlayerEntity) {
                    stack.decrement(1);
                    int rand = new Random().nextInt(10);
                    if(rand == 5){
                        world.spawnEntity(new ItemEntity(world,user.getX(),user.getY(),user.getZ(),
                                modItemRegistry.BASE_WAND.getDefaultStack()));
                    }
                    world.spawnEntity(new ItemEntity(world,user.getX(),user.getY(),user.getZ(),
                            modItemRegistry.WAND_TABLE.getDefaultStack()));
                }
            }
        }
    }

    @Override

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);

    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,controllerName,1,this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {

            user.setCurrentHand(hand);
            final int id = GeckoLibUtil.guaranteeIDForStack(user.getStackInHand(hand), (ServerWorld) world);

            GeckoLibNetwork.syncAnimation(user, this, id, ANIM_OPEN);

            for (PlayerEntity otherPlayer : PlayerLookup.tracking(user)) {
                GeckoLibNetwork.syncAnimation(otherPlayer, this, id, ANIM_OPEN);
            }

        }
        return TypedActionResult.consume(itemStack);

    }



    @Override
    public boolean hasGlint(ItemStack stack) {
        return super.hasGlint(stack);
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_OPEN) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("open",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));

        }
    }


}
