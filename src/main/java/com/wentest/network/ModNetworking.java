package com.wentest.network;

import com.wentest.WentestMod;
import com.wentest.client.hud.WindHudOverlay;
import com.wentest.server.flight.WindFlightController;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class ModNetworking {
    public static final Identifier REQUEST_TOGGLE_BOOST_C2S = WentestMod.id("request_toggle_boost");
    public static final Identifier SYNC_WIND_STATE_S2C = WentestMod.id("sync_wind_state");

    private ModNetworking() {}

    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_TOGGLE_BOOST_C2S, (server, player, handler, buf, responseSender) ->
                server.execute(() -> WindFlightController.handleToggleRequest(player)));
    }

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_WIND_STATE_S2C, (client, handler, buf, responseSender) -> {
            WindFlightState state = WindFlightState.valueOf(buf.readString());
            int pressure = buf.readVarInt();
            int cooldown = buf.readVarInt();
            boolean hasFuel = buf.readBoolean();
            client.execute(() -> WindHudOverlay.update(state, pressure, cooldown, hasFuel));
        });
    }

    public static PacketByteBuf buildWindSyncPayload(WindFlightState state, int pressure, int cooldown, boolean hasFuel) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(state.name());
        buf.writeVarInt(pressure);
        buf.writeVarInt(cooldown);
        buf.writeBoolean(hasFuel);
        return buf;
    }
}
