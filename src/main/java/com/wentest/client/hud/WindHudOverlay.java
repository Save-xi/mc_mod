package com.wentest.client.hud;

import com.wentest.compat.TrinketsCompat;
import com.wentest.network.packet.ClientWindState;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class WindHudOverlay {
    private WindHudOverlay() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register(WindHudOverlay::render);
    }

    private static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !TrinketsCompat.hasSkyBeltEquipped(client.player)) {
            return;
        }

        if (ClientWindState.mode() == WindFlightState.IDLE && ClientWindState.cooldownTicksRemaining() <= 0) {
            return;
        }

        int x = 8;
        int y = client.getWindow().getScaledHeight() - 28;
        int barWidth = 81;
        int fill = Math.min(barWidth, Math.max(0, ClientWindState.windPressure() * barWidth / 100));

        context.fill(x, y, x + barWidth, y + 8, 0xAA222222);
        context.fill(x, y, x + fill, y + 8, 0xAA6FD3FF);
        context.drawText(client.textRenderer,
                Text.literal("Wind: " + ClientWindState.mode() + " CD:" + ClientWindState.cooldownTicksRemaining()),
                x,
                y - 10,
                0xFFFFFF,
                false);
    }
}
