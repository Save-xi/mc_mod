package com.wentest.server.flight;

public enum WindFlightState {
    IDLE,
    BOOST,
    GLIDE,
    COOLDOWN;

    public boolean isFlyingState() {
        return this == BOOST || this == GLIDE;
    }
}
