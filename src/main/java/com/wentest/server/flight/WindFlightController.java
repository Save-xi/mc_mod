package com.wentest.server.flight;

import com.wentest.compat.TrinketsCompat;
import com.wentest.network.packet.SyncWindStateS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class WindFlightController {
    private static final int MAX_WIND_PRESSURE = 100;
    private static final int COOLDOWN_TICKS = 60;
    private static final WindFlightController INSTANCE = new WindFlightController();

    private final Map<UUID, WindFlightData> states = new ConcurrentHashMap<>();

    private WindFlightController() {
    }

    public static WindFlightController get() {
        return INSTANCE;
    }

    public void handleToggleRequest(ServerPlayerEntity player) {
        WindFlightData data = states.computeIfAbsent(player.getUuid(), ignored -> WindFlightData.create());

        if (!TrinketsCompat.hasSkyBelt(player) || player.isOnGround() || data.mode == WindFlightState.COOLDOWN) {
            return;
        }

        data.mode = data.mode == WindFlightState.BOOST ? WindFlightState.GLIDE : WindFlightState.BOOST;
        sync(player, data);
    }

    public void tick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            WindFlightData data = states.computeIfAbsent(player.getUuid(), ignored -> WindFlightData.create());
            updatePlayerState(player, data);
        }
    }

    private void updatePlayerState(ServerPlayerEntity player, WindFlightData data) {
        if (!TrinketsCompat.hasSkyBelt(player)) {
            if (data.mode != WindFlightState.IDLE) {
                data.mode = WindFlightState.IDLE;
                sync(player, data);
            }
            return;
        }

        WindFlightState before = data.mode;

        if (data.mode == WindFlightState.BOOST) {
            player.addVelocity(0.0D, 0.08D, 0.0D);
            Vec3d v = player.getVelocity();
            player.setVelocity(v.x, Math.min(v.y, 0.6D), v.z);
            player.velocityModified = true;
            player.fallDistance = 0.0F;

            data.windPressure = Math.min(MAX_WIND_PRESSURE, data.windPressure + 1);
            if (data.windPressure >= MAX_WIND_PRESSURE) {
                data.mode = WindFlightState.COOLDOWN;
                data.cooldownTicksRemaining = COOLDOWN_TICKS;
            }
        } else if (data.mode == WindFlightState.GLIDE) {
            Vec3d v = player.getVelocity();
            if (v.y < -0.15D) {
                player.setVelocity(v.x, -0.15D, v.z);
                player.velocityModified = true;
            }
            player.fallDistance = 0.0F;
            data.windPressure = Math.max(0, data.windPressure - 1);
        } else if (data.mode == WindFlightState.COOLDOWN) {
            data.cooldownTicksRemaining = Math.max(0, data.cooldownTicksRemaining - 1);
            data.windPressure = Math.max(0, data.windPressure - 2);
            if (data.cooldownTicksRemaining == 0) {
                data.mode = WindFlightState.IDLE;
            }
        } else {
            data.windPressure = Math.max(0, data.windPressure - 1);
            if (!player.isOnGround() && data.windPressure > 0) {
                data.mode = WindFlightState.GLIDE;
            }
        }

        if (player.isOnGround() && data.mode != WindFlightState.COOLDOWN) {
            data.mode = WindFlightState.IDLE;
        }

        if (before != data.mode || player.age % 10 == 0) {
            sync(player, data);
        }
    }

    private void sync(ServerPlayerEntity player, WindFlightData data) {
        ServerPlayNetworking.send(player, new SyncWindStateS2C(data.mode, data.windPressure, data.cooldownTicksRemaining));
    }

    private static final class WindFlightData {
        private WindFlightState mode;
        private int windPressure;
        private int cooldownTicksRemaining;

        private static WindFlightData create() {
            WindFlightData data = new WindFlightData();
            data.mode = WindFlightState.IDLE;
            data.windPressure = 0;
            data.cooldownTicksRemaining = 0;
            return data;
        }
    }
}
