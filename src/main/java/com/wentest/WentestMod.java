package com.wentest;

import com.wentest.network.packet.RequestToggleBoostC2S;
import com.wentest.registry.ModItems;
import com.wentest.server.flight.WindFlightController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WentestMod implements ModInitializer {
    public static final String MOD_ID = "wentest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.register();

        ServerPlayNetworking.registerGlobalReceiver(RequestToggleBoostC2S.TYPE, (packet, player, responseSender) ->
            WindFlightController.get().handleToggleRequest(player)
        );

        ServerTickEvents.END_SERVER_TICK.register(server -> WindFlightController.get().tick(server));
        LOGGER.info("Echoes of the Sky initialized");
    }
}
