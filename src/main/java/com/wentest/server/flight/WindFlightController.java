package com.wentest.server.flight;

import com.wentest.WentestMod;
import com.wentest.compat.TrinketsCompat;
import com.wentest.network.packet.SyncWindStateS2C;
import net.fabricmc.fabric.api.entity.event.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WindFlightController {
    private static final int MAX_PRESSURE = 100;
    private static final int COOLDOWN_TICKS = 100;
    private static final int PRESSURE_RATE = 2;
    private static final int GLIDE_RECOVERY_RATE = 1;

    private static final Map<UUID, Data> STATES = new HashMap<>();

    private WindFlightController() {}

    public static void registerTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                tickPlayer(player);
            }
        });
    }

    public static void handleToggleRequest(ServerPlayerEntity player) {
        Data data = getData(player);
        if (!isAllowedToFly(player, data)) {
            setIdle(player, data);
            return;
        }

        if (data.state == WindFlightState.BOOST) {
            data.state = WindFlightState.GLIDE;
        } else if (data.state == WindFlightState.GLIDE || data.state == WindFlightState.IDLE) {
            if (consumeFuel(player)) {
                data.state = WindFlightState.BOOST;
            }
        }

        sync(player, data);
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        Data data = getData(player);

        if (!isAllowedToFly(player, data)) {
            if (data.state != WindFlightState.IDLE) {
                setIdle(player, data);
            }
            return;
        }

        if (data.cooldownTicks > 0) {
            data.cooldownTicks--;
            data.state = WindFlightState.COOLDOWN;
            if (data.cooldownTicks == 0) {
                data.state = WindFlightState.IDLE;
                data.windPressure = 0;
            }
            sync(player, data);
            return;
        }

        if (data.state == WindFlightState.BOOST) {
            applyBoostMotion(player);
            data.windPressure = Math.min(MAX_PRESSURE, data.windPressure + PRESSURE_RATE);
            if (data.windPressure >= MAX_PRESSURE) {
                data.state = WindFlightState.COOLDOWN;
                data.cooldownTicks = COOLDOWN_TICKS;
            }
            sync(player, data);
        } else if (data.state == WindFlightState.GLIDE) {
            applyGlideMotion(player);
            data.windPressure = Math.max(0, data.windPressure - GLIDE_RECOVERY_RATE);
            sync(player, data);
        }
    }

    private static boolean isAllowedToFly(ServerPlayerEntity player, Data data) {
        return TrinketsCompat.hasSkyBeltEquipped(player)
            && !player.isOnGround()
            && !player.isTouchingWater()
            && !player.isInLava()
            && data.cooldownTicks <= 0;
    }

    private static void setIdle(ServerPlayerEntity player, Data data) {
        data.state = WindFlightState.IDLE;
        data.cooldownTicks = 0;
        data.windPressure = 0;
        sync(player, data);
    }

    private static void applyBoostMotion(ServerPlayerEntity player) {
        double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 1.3D;
        player.addVelocity(player.getRotationVec(1.0F).multiply(speed).add(0, 0.05D, 0));
        player.velocityModified = true;
    }

    private static void applyGlideMotion(ServerPlayerEntity player) {
        if (player.getVelocity().y < -0.08D) {
            player.setVelocity(player.getVelocity().x, -0.08D, player.getVelocity().z);
            player.velocityModified = true;
        }
    }

    private static boolean consumeFuel(ServerPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            var stack = player.getInventory().getStack(i);
            if (WentestMod.isWindFuel(stack.getItem())) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }

    private static Data getData(ServerPlayerEntity player) {
        return STATES.computeIfAbsent(player.getUuid(), u -> new Data());
    }

    private static void sync(ServerPlayerEntity player, Data data) {
        ServerPlayNetworking.send(player, new SyncWindStateS2C(data.state, data.windPressure, data.cooldownTicks, hasFuel(player)));
    }

    private static boolean hasFuel(ServerPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            if (WentestMod.isWindFuel(player.getInventory().getStack(i).getItem())) {
                return true;
            }
        }
        return false;
    }

    private static final class Data {
        private WindFlightState state = WindFlightState.IDLE;
        private int windPressure = 0;
        private int cooldownTicks = 0;
    }
}
