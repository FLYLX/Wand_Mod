package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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

public class animated_base_wand extends Item implements  IAnimatable, ISyncable {


    public static final String controllerName = "base_controller";
    public static final int ANIM_OPEN = 1;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public long MAX = Long.MAX_VALUE;


    public animated_base_wand(Settings settings) {
        super(settings.maxDamage(201));
        GeckoLibNetwork.registerSyncable(this);
//        MinecraftClient.getInstance().getClass();

    }
    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }



    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) user;
            if (!world.isClient()) {
                playerentity.getItemCooldownManager().set(this,50);
                }
            }

    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if(!world.isClient){
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 9.0f) {
                if(user instanceof  PlayerEntity) {

                    PlayerEntity playerentity = (PlayerEntity) user;


                    BasicMagic basicMagic = new BasicMagic(world, playerentity);

                    basicMagic.setVelocity(playerentity, playerentity.getPitch(), playerentity.getYaw(), 0F, 1.0F,
                            0F);

                    basicMagic.age = 30;

                    basicMagic.hasNoGravity();

                    basicMagic.setOwnerItem(stack);
                    
                    world.spawnEntity(basicMagic);
            }
        }
        }
    }

    @Override

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);

//        final int id = GeckoLibUtil.getIDFromStack(stack);
//        final AnimationController<animated_base_wand> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
//            controller.markNeedsReload();
            //写了会有bug
            //controller.setAnimation(new AnimationBuilder().addAnimation("base_wand",ILoopType.EDefaultLoopTypes
            // .LOOP));



    }
//    private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack) {
//        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
//            return new MagicScreenHandler(syncId, inventory);
//        }, stack.getName());
//    }

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
        world.playSound(null, user.getBlockPos(), ModSounds.WAND_SWING, SoundCategory.PLAYERS, 2f, 1f);
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
            controller.setAnimation(new AnimationBuilder().addAnimation("magic",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));

        }
    }



}
