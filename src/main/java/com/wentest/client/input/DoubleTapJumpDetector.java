package com.wentest.client.input;

import com.wentest.network.ModNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class DoubleTapJumpDetector {
    private static final int WINDOW_TICKS = 8;
    private static int lastJumpPressTick = -100;
    private static boolean lastPressed;

    private DoubleTapJumpDetector() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(DoubleTapJumpDetector::tick);
    }

    private static void tick(MinecraftClient client) {
        if (client.player == null || client.options == null) {
            return;
        }

        boolean pressed = client.options.jumpKey.isPressed();
        if (pressed && !lastPressed) {
            int now = client.player.age;
            if (!client.player.isOnGround() && now - lastJumpPressTick <= WINDOW_TICKS) {
                ClientPlayNetworking.send(ModNetworking.REQUEST_TOGGLE_BOOST_C2S, net.fabricmc.fabric.api.networking.v1.PacketByteBufs.empty());
            }
            lastJumpPressTick = now;
        }

        lastPressed = pressed;
    }
}
