package com.wentest.client.hud;

import com.wentest.compat.TrinketsCompat;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class WindHudOverlay {
    private static WindFlightState state = WindFlightState.IDLE;
    private static int pressure;
    private static int cooldown;
    private static boolean hasFuel;

    private WindHudOverlay() {}

    public static void register() {
        HudRenderCallback.EVENT.register(WindHudOverlay::render);
    }

    public static void update(WindFlightState nextState, int nextPressure, int nextCooldown, boolean nextHasFuel) {
        state = nextState;
        pressure = nextPressure;
        cooldown = nextCooldown;
        hasFuel = nextHasFuel;
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !TrinketsCompat.hasSkyBelt(client.player)) {
            return;
        }
        if (state == WindFlightState.IDLE && cooldown <= 0) {
            return;
        }

        int x = 12;
        int y = context.getScaledWindowHeight() - 40;
        context.fill(x, y, x + 104, y + 10, 0xAA222222);
        context.fill(x + 2, y + 2, x + 2 + pressure, y + 8, 0xAA66CCFF);
        context.drawText(client.textRenderer, "风压 " + pressure + "%", x, y - 10, 0xFFFFFF, true);
        context.drawText(client.textRenderer, "状态: " + state.name() + (hasFuel ? "" : " (无燃料)"), x, y + 12, 0xEEEEEE, false);
    }
}
