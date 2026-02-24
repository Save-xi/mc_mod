package com.wentest.client.input;

import com.wentest.network.packet.RequestToggleBoostC2S;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class DoubleTapJumpDetector {
    private static final int WINDOW_TICKS = 8;

    private static int tickCounter = 0;
    private static int lastJumpPressTick = -100;
    private static boolean wasPressed = false;

    private DoubleTapJumpDetector() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DoubleTapJumpDetector::onTick);
    }

    private static void onTick(MinecraftClient client) {
        tickCounter++;
        if (client.player == null) {
            return;
        }

        boolean pressed = client.options.jumpKey.isPressed();
        if (pressed && !wasPressed) {
            if (!client.player.isOnGround() && tickCounter - lastJumpPressTick <= WINDOW_TICKS) {
                ClientPlayNetworking.send(new RequestToggleBoostC2S());
            }
            lastJumpPressTick = tickCounter;
        }
        wasPressed = pressed;
    }
}
