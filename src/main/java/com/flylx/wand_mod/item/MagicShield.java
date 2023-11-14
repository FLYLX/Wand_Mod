package com.flylx.wand_mod.item;

import com.flylx.wand_mod.entity.MagicShieldEffect;
import net.minecraft.entity.player.PlayerEntity;
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

public class MagicShield extends Item implements IAnimatable, ISyncable {
    public static final int ANIM_OPEN = 1;
    public static final String controllerName = "shield_controller";
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public MagicShield(Settings settings) {
        super(settings);
        GeckoLibNetwork.registerSyncable(this);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient()) {
            final int id = GeckoLibUtil.guaranteeIDForStack(user.getStackInHand(hand), (ServerWorld) world);

            GeckoLibNetwork.syncAnimation(user, this, id, ANIM_OPEN);

            MagicShieldEffect magicShieldEffect = new MagicShieldEffect(world,user.prevX,user.prevY,user.prevZ);
            magicShieldEffect.setRadius(2.0f);
            magicShieldEffect.setRadiusGrowth(-0.5f);
            magicShieldEffect.setOwner(user);
            world.spawnEntity(magicShieldEffect);
        }
        return TypedActionResult.consume(itemStack);

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
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_OPEN) {
            final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("spin",
                    ILoopType.EDefaultLoopTypes.LOOP));

        }
    }
}
