package com.wentest.client.input;

import com.wentest.network.packet.ModPackets;
import com.wentest.network.packet.RequestToggleBoostC2SPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public final class DoubleTapJumpDetector {
    private static final int DOUBLE_TAP_WINDOW_TICKS = 8;

    private static long tickCounter = 0;
    private static long lastJumpPressTick = Long.MIN_VALUE;
    private static boolean jumpPressedLastTick = false;

    private DoubleTapJumpDetector() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DoubleTapJumpDetector::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        tickCounter++;

        if (client.player == null || client.currentScreen != null) {
            jumpPressedLastTick = false;
            return;
        }

        boolean jumpPressedNow = client.options.jumpKey.isPressed();
        boolean risingEdge = jumpPressedNow && !jumpPressedLastTick;

        if (risingEdge && !client.player.isOnGround()) {
            if (tickCounter - lastJumpPressTick <= DOUBLE_TAP_WINDOW_TICKS) {
                ModPackets.sendToServer(new RequestToggleBoostC2SPayload());
            }
            lastJumpPressTick = tickCounter;
        }

        jumpPressedLastTick = jumpPressedNow;
    }
}
