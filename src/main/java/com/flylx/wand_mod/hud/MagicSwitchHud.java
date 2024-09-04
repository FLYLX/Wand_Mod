package com.flylx.wand_mod.hud;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.event.KeyInputHandler;
import com.flylx.wand_mod.item.modItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

public class MagicSwitchHud implements HudRenderCallback {
    private static final Identifier BACKGROUND = new Identifier(Wand_mod.ModID, "textures/hud/switch_background.png");

    private static float degree = 0;

    public static float getHubDegree() { return degree; }

    public static void setHudDegree(float value) {
        if (value < 0)
            degree = (value % 360 + 360) % 360;
        else
            degree = value % 360;
    }

    private static final Identifier[] MagicIcon = {
        new Identifier(Wand_mod.ModID, "textures/hud/explosion_magic.png"),
        new Identifier(Wand_mod.ModID, "textures/hud/froze_magic.png"),
        new Identifier(Wand_mod.ModID, "textures/hud/poison_magic.png"),
        new Identifier(Wand_mod.ModID, "textures/hud/heart_magic.png"),
        new Identifier(Wand_mod.ModID, "textures/hud/claw_magic.png"),
        new Identifier(Wand_mod.ModID, "textures/hud/stone_magic.png")
    };

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null)
            return;
        if (!KeyInputHandler.ISPRESS_R)
            return;

        ClientPlayerEntity player = client.player;
        if (player == null)
            return;
        if (!player.getMainHandStack().isOf(modItemRegistry.BASE_WAND)
         && !player.getOffHandStack().isOf(modItemRegistry.BASE_WAND))
            return;

        double width = client.getWindow().getScaledWidth();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.push();
        matrixStack.translate(width, 0, 0);
        matrixStack.scale(2, 2, 1);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        DrawableHelper.drawTexture(matrixStack, -32, 0, 0, 0, 32, 32, 32, 32);
        matrixStack.pop();

        int magic0 = ((int) (getHubDegree() / 60) + 5) % 6;
        int magic1 = ((int) (getHubDegree() / 60)    ) % 6;
        int magic2 = ((int) (getHubDegree() / 60) + 1) % 6;

        matrixStack.push();
        matrixStack.translate(width - 30 * 2, -10 * 2, 0);
        matrixStack.scale(2, 2, 1);
        RenderSystem.setShaderTexture(0, MagicIcon[magic0]);
        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, 16, 16, 16, 16);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(width - 27 * 2, 11 * 2, 0);
        matrixStack.scale(2, 2, 1);
        RenderSystem.setShaderTexture(0, MagicIcon[magic1]);
        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, 16, 16, 16, 16);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(width - 5 * 2, 14 * 2, 0);
        matrixStack.scale(2, 2, 1);
        RenderSystem.setShaderTexture(0, MagicIcon[magic2]);
        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, 16, 16, 16, 16);
        matrixStack.pop();
    }
}
