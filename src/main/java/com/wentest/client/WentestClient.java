package com.wentest.client;

import com.wentest.client.hud.WindHudOverlay;
import com.wentest.client.input.DoubleTapJumpDetector;
import com.wentest.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;

public class WentestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModNetworking.registerS2C();
        DoubleTapJumpDetector.register();
        WindHudOverlay.register();
    }
}
