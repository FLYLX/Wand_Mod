package com.flylx.wand_mod.render;

import com.flylx.wand_mod.entity.WandTableEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;



public class WandTableEntityRenderer implements BlockEntityRenderer<WandTableEntity> {
    private final ItemRenderer itemRenderer;
    private final BlockRenderManager blockRenderManager;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;


    public WandTableEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(WandTableEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.isEmpty()) {
            return;
        }


        Direction direction = entity.getCachedState().get(FACING);

        switch (direction){
            case NORTH:
                matrices.push();

                matrices.translate(0.675f,0.5f,0.345f);
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-43));
                matrices.scale(0.6f, 0.6f, 0.6f);

                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getContent(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);

                matrices.pop();

                return;
            case WEST:
                matrices.push();
                matrices.translate(0.365f,0.5f,0.345f);

                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-43));
                matrices.scale(0.6f, 0.6f, 0.6f);

                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getContent(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);

                matrices.pop();
                return;
            case EAST:
                matrices.push();
                matrices.translate(0.675f,0.5f,0.655f);

                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-43));
                matrices.scale(0.6f, 0.6f, 0.6f);

                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getContent(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);

                matrices.pop();
                return;
            case SOUTH:
                matrices.push();
                matrices.translate(0.365f,0.5f,0.655f);

                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-43));
                matrices.scale(0.6f, 0.6f, 0.6f);

                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getContent(), ModelTransformation.Mode.GUI,
                        light,
                        overlay
                        , matrices, vertexConsumers, 0);

                matrices.pop();
                return;
        }



    }

}
