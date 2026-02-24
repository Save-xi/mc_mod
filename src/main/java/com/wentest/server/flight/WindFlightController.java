package com.wentest.server.flight;

import com.wentest.compat.TrinketsCompat;
import com.wentest.network.ModNetworking;
import com.wentest.registry.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WindFlightController {
    private static final Map<UUID, WindRuntime> RUNTIME = new HashMap<>();

    private WindFlightController() {}

    public static void handleToggleRequest(ServerPlayerEntity player) {
        WindRuntime runtime = RUNTIME.computeIfAbsent(player.getUuid(), id -> new WindRuntime());
        if (!TrinketsCompat.hasSkyBelt(player) || player.isOnGround() || runtime.cooldownTicks > 0) {
            return;
        }

        if (runtime.state == WindFlightState.IDLE || runtime.state == WindFlightState.GLIDE) {
            if (consumeFuel(player)) {
                runtime.state = WindFlightState.BOOST;
            }
        } else if (runtime.state == WindFlightState.BOOST) {
            runtime.state = WindFlightState.GLIDE;
        }

        sync(player, runtime);
    }

    public static void tickServer(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            WindRuntime runtime = RUNTIME.computeIfAbsent(player.getUuid(), id -> new WindRuntime());
            tickPlayer(player, runtime);
        }
    }

    private static void tickPlayer(ServerPlayerEntity player, WindRuntime runtime) {
        if (player.isOnGround()) {
            runtime.state = WindFlightState.IDLE;
            runtime.pressure = 0;
        }

        if (runtime.cooldownTicks > 0) {
            runtime.cooldownTicks--;
            runtime.state = runtime.cooldownTicks == 0 ? WindFlightState.IDLE : WindFlightState.COOLDOWN;
        } else if (runtime.state == WindFlightState.BOOST) {
            runtime.pressure += 2;
            player.addVelocity(0.0, 0.03, 0.0);
            player.velocityModified = true;
            if (runtime.pressure >= 100) {
                runtime.pressure = 100;
                runtime.state = WindFlightState.COOLDOWN;
                runtime.cooldownTicks = 100;
            }
        } else if (runtime.state == WindFlightState.GLIDE) {
            runtime.pressure = Math.max(0, runtime.pressure - 1);
        }

        if (player.age % 10 == 0) {
            sync(player, runtime);
        }
    }

    private static void sync(ServerPlayerEntity player, WindRuntime runtime) {
        ServerPlayNetworking.send(player, ModNetworking.SYNC_WIND_STATE_S2C,
                ModNetworking.buildWindSyncPayload(runtime.state, runtime.pressure, runtime.cooldownTicks, hasFuel(player)));
    }

    private static boolean consumeFuel(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(ModItems.WIND_CRYSTAL)) {
                stack.decrement(1);
                return true;
            }
        }
        return false;
    }

    private static boolean hasFuel(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            if (player.getInventory().getStack(i).isOf(ModItems.WIND_CRYSTAL)) {
                return true;
            }
        }
        return false;
    }

    private static class WindRuntime {
        private WindFlightState state = WindFlightState.IDLE;
        private int pressure;
        private int cooldownTicks;
    }
}
