package com.wentest.client;

import com.wentest.client.hud.WindHudOverlay;
import com.wentest.client.input.DoubleTapJumpDetector;
import com.wentest.network.packet.SyncWindStateS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class WentestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DoubleTapJumpDetector.register();
        WindHudOverlay.register();

        ClientPlayNetworking.registerGlobalReceiver(SyncWindStateS2C.ID, (payload, context) ->
            context.client().execute(() -> {
                WindClientState.state = payload.state();
                WindClientState.windPressure = payload.windPressure();
                WindClientState.cooldownTicks = payload.cooldownTicks();
                WindClientState.hasFuel = payload.hasFuel();
            })
        );
    }
}
