package com.flylx.wand_mod.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    private static final VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape CHECK_SHAPE = VoxelShapes.union(ABOVE_SHAPE);

    @Shadow public abstract ItemStack getStack();


    @Shadow public abstract ItemEntity copy();

    @Inject(at = @At("HEAD"), method = "tick()V")
    protected void ontick(CallbackInfo cir){
        if(!((ItemEntity)(Object)this).world.isClient) {
            if (this.getStack().isOf(modItemRegistry.EMPTY_SCROLL)) {
                //resolve this
                checkItem((ItemEntity) (Object) this);
            }
        }
    }

    public void checkItem(ItemEntity itemEntity){

        for (ItemEntity itemEntity1 : checkItemEntities(((ItemEntity)(Object)this).world, itemEntity)) {
            if(itemEntity1.getStack().isOf(Items.WATER_BUCKET)){
                ((ItemEntity) (Object) this).world.spawnEntity(new ItemEntity(((ItemEntity)(Object)this).world,
                        ((ItemEntity)(Object)this).getX(), ((ItemEntity)(Object)this).getY(),
                        ((ItemEntity)(Object)this).getZ(),modItemRegistry.FROZE_SCROLL.getDefaultStack()));
                ((ItemEntity) (Object) this).discard();
                itemEntity1.discard();
            }
        }


    }

    private static List<ItemEntity> checkItemEntities(World world, ItemEntity itemEntity) {

        return ABOVE_SHAPE.getBoundingBoxes().stream().flatMap(new Function<Box, Stream<? extends ItemEntity>>() {
            @Override
            public Stream<? extends ItemEntity> apply(Box box) {
                return world.getEntitiesByClass(ItemEntity.class,
                        box.offset(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ()),
                        EntityPredicates.VALID_ENTITY).stream();
            }
        }).collect(Collectors.toList());
    }
}
