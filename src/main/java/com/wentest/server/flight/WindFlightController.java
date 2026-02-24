package com.wentest.server.flight;

import com.wentest.network.packet.ModPackets;
import com.wentest.network.packet.SyncWindStateS2CPayload;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.network.ServerPlayerEntity;

public final class WindFlightController {
    private static final int MAX_PRESSURE = 100;
    private static final int OVERHEAT_COOLDOWN_TICKS = 60;
    private static final Map<UUID, StateData> DATA = new ConcurrentHashMap<>();

    private WindFlightController() {
    }

    public static void tick(ServerPlayerEntity player) {
        StateData data = DATA.computeIfAbsent(player.getUuid(), ignored -> new StateData());

        if (player.isOnGround()) {
            data.mode = WindFlightState.IDLE;
            data.cooldownTicksRemaining = 0;
            data.windPressure = 0;
            sync(player, data);
            return;
        }

        if (data.mode == WindFlightState.BOOST || data.mode == WindFlightState.GLIDE) {
            data.windPressure = Math.min(MAX_PRESSURE, data.windPressure + 1);
        } else if (data.windPressure > 0) {
            data.windPressure--;
        }

        if (data.windPressure >= MAX_PRESSURE) {
            data.mode = WindFlightState.COOLDOWN;
            data.cooldownTicksRemaining = OVERHEAT_COOLDOWN_TICKS;
            sync(player, data);
        }

        if (data.mode == WindFlightState.COOLDOWN && data.cooldownTicksRemaining > 0) {
            data.cooldownTicksRemaining--;
            if (data.cooldownTicksRemaining == 0) {
                data.mode = WindFlightState.IDLE;
                data.windPressure = 0;
            }
        }
    }

    public static void toggleBoost(ServerPlayerEntity player) {
        StateData data = DATA.computeIfAbsent(player.getUuid(), ignored -> new StateData());

        if (data.mode == WindFlightState.COOLDOWN || player.isOnGround()) {
            return;
        }

        data.mode = data.mode == WindFlightState.BOOST ? WindFlightState.GLIDE : WindFlightState.BOOST;
        sync(player, data);
    }

    private static void sync(ServerPlayerEntity player, StateData data) {
        ModPackets.sendToClient(player, new SyncWindStateS2CPayload(data.mode, data.windPressure, data.cooldownTicksRemaining));
    }

    private static final class StateData {
        private WindFlightState mode = WindFlightState.IDLE;
        private int windPressure = 0;
        private int cooldownTicksRemaining = 0;
    }
}
