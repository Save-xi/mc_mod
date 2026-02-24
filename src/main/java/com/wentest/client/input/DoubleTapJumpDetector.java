package com.wentest.client.input;

import com.wentest.network.packet.RequestToggleBoostC2S;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class DoubleTapJumpDetector {
    private static final int DOUBLE_TAP_WINDOW = 8;
    private static long tick;
    private static long lastJumpPressTick = -100;
    private static boolean wasPressed;

    private DoubleTapJumpDetector() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DoubleTapJumpDetector::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        tick++;
        if (client.player == null || client.currentScreen != null) {
            wasPressed = false;
            return;
        }

        boolean isPressed = client.options.jumpKey.isPressed();
        if (isPressed && !wasPressed) {
            if (!client.player.isOnGround() && tick - lastJumpPressTick <= DOUBLE_TAP_WINDOW) {
                ClientPlayNetworking.send(new RequestToggleBoostC2S());
            }
            lastJumpPressTick = tick;
        }

        wasPressed = isPressed;
    }
}
