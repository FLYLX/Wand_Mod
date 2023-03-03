package com.flylx.wand_mod.hud;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.event.KeyInputHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import org.apache.logging.log4j.LogManager;

public class MagicSwitchHud implements HudRenderCallback {
    private static final Identifier VIEWFINDER = new Identifier(Wand_mod.ModID, "textures/hud/switch_menu.png");
    public static Quaternion quaternion = new Quaternion(0,0,0,0);

    public void setQuaternion(Quaternion quaternion){
        this.quaternion = new Quaternion(quaternion.getX()+this.quaternion.getX(),
                quaternion.getY()+this.quaternion.getY(),quaternion.getZ()+this.quaternion.getZ(),
                quaternion.getW());

    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        //贴图的宽度
        float imageWidth = 64;
        //贴图高度
        float imageHeight = 64;
        float hnew=0;
        float wnew=0;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth()/2;
            int height = client.getWindow().getScaledHeight()/2;

            float rs = width / height;
            float ri = imageWidth / imageHeight;


            if (rs > ri) {
                wnew = width;
                hnew = height;
            } else {
                wnew = width;
                hnew = height;
            }
            x = Math.round((height - hnew) / 2F);
            y = Math.round((width - wnew) / 2F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, VIEWFINDER);

            if(KeyInputHandler.ISPRESS_R == true){
                matrixStack.push();
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
                matrixStack.multiply(quaternion);
                DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, Math.round(wnew),
                                Math.round(wnew), Math.round(wnew), Math.round(wnew));
                matrixStack.pop();
                    }else {

            }
        }
    }
}
