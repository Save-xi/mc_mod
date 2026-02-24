package com.wentest.client;

import com.wentest.client.hud.WindHudOverlay;
import com.wentest.client.input.DoubleTapJumpDetector;
import com.wentest.network.packet.ModPackets;
import net.fabricmc.api.ClientModInitializer;

public class WentestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPackets.registerClientReceivers();
        DoubleTapJumpDetector.register();
        WindHudOverlay.register();
    }
}
