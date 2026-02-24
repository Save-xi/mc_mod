package com.wentest.client;

import com.wentest.server.flight.WindFlightState;

public final class WindClientState {
    public static WindFlightState state = WindFlightState.IDLE;
    public static int windPressure = 0;
    public static int cooldownTicks = 0;
    public static boolean hasFuel = false;

    private WindClientState() {}
}
