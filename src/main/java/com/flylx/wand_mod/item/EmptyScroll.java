package com.flylx.wand_mod.item;


import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
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

import java.util.List;
import java.util.stream.Collectors;

public class EmptyScroll extends Item implements IAnimatable, ISyncable {

//    public static final VoxelShape INSIDE_SHAPE = Block.createCuboidShape(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0, 16.0, 0.0, 16.0, 32.0, 16.0);
    public static final VoxelShape CHECK_SHAPE = VoxelShapes.union(ABOVE_SHAPE);


    public static final String controllerName = "controller";
    public static final int ANIM_OPEN = 0;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static boolean LEFT_CLICK = false;

    public EmptyScroll(Settings settings) {
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

        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemEntity itemEntity = new ItemEntity(EntityType.ITEM,world);


        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if(user.getOffHandStack().isOf(modItemRegistry.EMPTY_SCROLL)) {

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

    public static List<ItemEntity> getInputItemEntities(World world, Hopper hopper) {
        return CHECK_SHAPE.getBoundingBoxes().stream().flatMap(
                box -> world.getEntitiesByClass(ItemEntity.class,
                        box.offset( - 0.5, hopper.getHopperY() - 0.5,
                                hopper.getHopperZ() - 0.5),
                        EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
    }

}
