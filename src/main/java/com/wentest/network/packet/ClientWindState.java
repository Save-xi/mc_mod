package com.wentest.network.packet;

import com.wentest.server.flight.WindFlightState;

public final class ClientWindState {
    private static WindFlightState mode = WindFlightState.IDLE;
    private static int windPressure;
    private static int cooldownTicksRemaining;

    private ClientWindState() {
    }

    public static WindFlightState mode() {
        return mode;
    }

    public static int windPressure() {
        return windPressure;
    }

    public static int cooldownTicksRemaining() {
        return cooldownTicksRemaining;
    }

    public static void update(SyncWindStateS2CPayload payload) {
        mode = payload.mode();
        windPressure = payload.windPressure();
        cooldownTicksRemaining = payload.cooldownTicksRemaining();
    }
}
