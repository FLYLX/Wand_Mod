package com.flylx.wand_mod.render;

import com.flylx.wand_mod.block.AltarBlock;
import com.flylx.wand_mod.entity.AltarEntity;
import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class AltarRenderer implements BlockEntityRenderer<AltarEntity> {


    public static final List<Item> render_map = new ArrayList<>(){
        {
            add(Items.AIR);
            add(modItemRegistry.FLAME_SCROLL);
            add(modItemRegistry.FROZE_SCROLL);
            add(modItemRegistry.CLAW_SCROLL);
            add(modItemRegistry.CURE_SCROLL);
            add(modItemRegistry.POISON_SCROLL);
            add(Items.DIAMOND_BLOCK);
            add(Items.EMERALD_BLOCK);
            add(Items.CHORUS_FRUIT);
            add(Items.STRING);
            add(Items.WHITE_WOOL);
        }
    };

    public AltarRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(AltarEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        if (blockState.get(AltarBlock.HAS_ITEM) == 0) {
            return;
        }
        Item item = render_map.get(blockState.get(AltarBlock.HAS_ITEM));

        matrices.push();

        matrices.translate(0.5f, 0.55f, 0.55f);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(9));
        matrices.scale(0.6f, 0.6f, 0.6f);


        MinecraftClient.getInstance().getItemRenderer().renderItem(item.getDefaultStack(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);
        matrices.pop();

    }
}