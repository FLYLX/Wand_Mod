package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.screen.MagicScreen;
import com.flylx.wand_mod.screen.MagicScreenHandler;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import software.bernie.example.entity.RocketProjectile;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class animated_base_wand extends Item implements  IAnimatable, ISyncable {


    public static final String controllerName = "controller";
    public static final int ANIM_OPEN = 1;
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public animated_base_wand(Settings settings) {
        super(settings.maxDamage(201));
        GeckoLibNetwork.registerSyncable(this);
        MinecraftClient.getInstance().getClass();

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
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(470);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PlayerEntity playerentity = (PlayerEntity) user;
                BasicMagic basicMagic = new BasicMagic(world, playerentity);

                basicMagic.setVelocity(playerentity, playerentity.getPitch(), playerentity.getYaw(), 0.0F, 1.0F * 2.0F,
                        1.0F);
                basicMagic.setDamage(5);
                basicMagic.age = 30;
                basicMagic.hasNoGravity();

                world.spawnEntity(basicMagic);

                stack.damage(1, playerentity, p -> p.sendToolBreakStatus(playerentity.getActiveHand()));

            }
        });
        t.start();

        super.onStoppedUsing(stack, world, user, remainingUseTicks);

    }


    @Override

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        final int id = GeckoLibUtil.getIDFromStack(stack);
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        if(controller.getAnimationState()==AnimationState.Stopped) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("base_wand",ILoopType.EDefaultLoopTypes.LOOP));
        }

    }
    private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
            return new MagicScreenHandler(syncId, inventory);
        }, stack.getName());
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
        return false;
    }

    @Override
    public void onAnimationSync(int id, int state) {

        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        if (state == ANIM_OPEN) {

            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("magic",
                    ILoopType.EDefaultLoopTypes.PLAY_ONCE));

        }
    }



}
