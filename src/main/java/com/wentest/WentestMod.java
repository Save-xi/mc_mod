package com.wentest;

import com.wentest.item.ModItems;
import com.wentest.network.packet.ModPackets;
import com.wentest.server.flight.WindFlightController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WentestMod implements ModInitializer {
    public static final String MOD_ID = "wentest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        ModPackets.registerServerReceivers();

        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerManager().getPlayerList().forEach(WindFlightController::tick));

        LOGGER.info("{} initialized", MOD_ID);
    }
}
