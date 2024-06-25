package com.flylx.wand_mod.armor;

import com.flylx.wand_mod.item.modItemRegistry;
import com.flylx.wand_mod.screen.MagicScreenHandHandler;
import com.flylx.wand_mod.screen.MagicScreenHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ScrollBeltItem extends ArmorItem implements IAnimatable, NamedScreenHandlerFactory{


    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public ScrollBeltItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);


    }
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));

    }


    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {

        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(!world.isClient) {
//            if (entity instanceof PlayerEntity) {
//                PlayerEntity user = (PlayerEntity) entity;
//                if (user.getEquippedStack(EquipmentSlot.CHEST).isOf(modItemRegistry.SCROLL_BELT_ITEM)) {
//                    NamedScreenHandlerFactory screenHandlerFactory = new SimpleNamedScreenHandlerFactory((syncId, inventory,
//                                                                                                          player) -> new MagicScreenHandler(syncId, inventory), Text.literal("aaa"));
//
//                    if (screenHandlerFactory != null) {
//                        // 这个调用会让服务器请求客户端开启合适的 Screenhandler
//                        user.openHandledScreen(screenHandlerFactory);
//
//                    }
//                }
//            }
        }
    }



    public boolean isSameInventory(DefaultedList<ItemStack> inventory, DefaultedList<ItemStack> inventory1){
        for(int i = 0;i<inventory.size();i++){
            if(inventory.get(i).getItem() != inventory1.get(i).getItem()||inventory.get(i).getCount()!=inventory1.get(i).getCount()){


                return false;
            }
        }
        return true;
    }


    @Override
    public boolean canBeNested() {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {

            if(user.getStackInHand(hand).isOf(modItemRegistry.SCROLL_BELT_ITEM)&&user.getEquippedStack(EquipmentSlot.CHEST).isOf(modItemRegistry.SCROLL_BELT_ITEM)) {

                NamedScreenHandlerFactory screenHandlerFactory = new SimpleNamedScreenHandlerFactory((syncId, inventory,
                                                                                                      player) -> new MagicScreenHandHandler(syncId, inventory), Text.literal("BeltInventory"));
                if (screenHandlerFactory != null) {
                    // 这个调用会让服务器请求客户端开启合适的 Screenhandler

                    user.openHandledScreen(screenHandlerFactory);
                }

            }
        }
        return super.use(world, user, hand);
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    public Text getDisplayName() {
        return null;
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MagicScreenHandler(syncId,inv);
    }


    @Override
    public void postProcessNbt(NbtCompound nbt) {

        super.postProcessNbt(nbt);

    }


}
