package com.wentest.client.hud;

import com.wentest.client.WindClientState;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class WindHudOverlay {
    private WindHudOverlay() {}

    public static void register() {
        HudRenderCallback.EVENT.register(WindHudOverlay::render);
    }

    private static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        if (WindClientState.state == WindFlightState.IDLE && WindClientState.cooldownTicks <= 0) {
            return;
        }

        int x = 10;
        int y = context.getScaledWindowHeight() - 40;
        int width = 100;
        int height = 8;

        context.fill(x, y, x + width, y + height, 0x66000000);
        int filled = Math.min(width, WindClientState.windPressure);
        context.fill(x, y, x + filled, y + height, 0xAA56C2FF);
        context.drawText(client.textRenderer,
            Text.literal("Wind: " + WindClientState.state + "  CD:" + WindClientState.cooldownTicks),
            x,
            y - 10,
            0xFFFFFF,
            true);
    }
}
