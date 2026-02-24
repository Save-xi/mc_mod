package com.wentest.client.hud;

import com.wentest.compat.TrinketsCompat;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class WindHudOverlay {
    private static WindFlightState mode = WindFlightState.IDLE;
    private static int windPressure;
    private static int cooldownTicksRemaining;

    private WindHudOverlay() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register(WindHudOverlay::render);
    }

    public static void update(WindFlightState m, int pressure, int cooldown) {
        mode = m;
        windPressure = pressure;
        cooldownTicksRemaining = cooldown;
    }

    private static void render(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !TrinketsCompat.hasSkyBelt(client.player)) {
            return;
        }
        if (mode == WindFlightState.IDLE && cooldownTicksRemaining <= 0) {
            return;
        }

        int width = 90;
        int height = 8;
        int x = 10;
        int y = client.getWindow().getScaledHeight() - 40;
        int fill = Math.max(0, Math.min(width, Math.round(width * (windPressure / 100.0f))));

        drawContext.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xCC000000);
        drawContext.fill(x, y, x + width, y + height, 0xFF2A2A2A);
        drawContext.fill(x, y, x + fill, y + height, mode == WindFlightState.COOLDOWN ? 0xFFB33A3A : 0xFF3AA6FF);
        drawContext.drawText(client.textRenderer, "风压 " + windPressure + "%", x, y - 10, 0xFFFFFFFF, true);
    }
}
