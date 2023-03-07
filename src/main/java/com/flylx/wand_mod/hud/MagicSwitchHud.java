package com.flylx.wand_mod.hud;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.util.IEntityDataSaver;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.apache.logging.log4j.LogManager;

public class MagicSwitchHud implements HudRenderCallback {
    private static final Identifier VIEWFINDER = new Identifier(Wand_mod.ModID, "textures/hud/switch_menu.png");
    public static float change = 0;

    public void setHudDegree(float degree){
        this.change = degree;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        //贴图的宽度
        float imageWidth = 64;
        //贴图高度
        float imageHeight = 64;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, VIEWFINDER);

            if(KeyInputHandler.ISPRESS_R == true){
                /**
                 * Draws a textured rectangle from a region in a texture.
                 *
                 * <p>The width and height of the region are the same as
                 * the dimensions of the rectangle.
                 *
                 * @param matrices the matrix stack used for rendering
                 * @param u the left-most coordinate of the texture region
                 * @param v the top-most coordinate of the texture region
                 * @param x the X coordinate of the rectangle
                 * @param y the Y coordinate of the rectangle
                 * @param textureWidth the width of the entire texture
                 * @param textureHeight the height of the entire texture
                 * @param width the width of the rectangle
                 * @param height the height of the rectangle
                 */

                matrixStack.push();

                matrixStack.translate(width,0,0);

                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(
                        ((IEntityDataSaver)client.getInstance().player).getPersistentData().getFloat("switch")));

                DrawableHelper.drawTexture(matrixStack, -64, -64, 0, 0,
                        128,128,128,128);

                matrixStack.pop();

                    }else {

            }
        }
    }
}
