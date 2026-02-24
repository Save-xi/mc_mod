package com.wentest.network.packet;

import com.wentest.compat.TrinketsCompat;
import com.wentest.server.flight.WindFlightController;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ModPackets {
    private ModPackets() {
    }

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(RequestToggleBoostC2SPayload.TYPE, ModPackets::onToggleBoostRequest);
        ServerPlayNetworking.registerGlobalReceiver(RequestDoubleJumpC2SPayload.TYPE,
                (packet, player, responseSender) -> {
                    // v0.2 预留：二段跳逻辑。
                });
    }

    private static void onToggleBoostRequest(RequestToggleBoostC2SPayload packet, ServerPlayerEntity player, PacketSender responseSender) {
        if (!TrinketsCompat.hasSkyBeltEquipped(player)) {
            return;
        }
        WindFlightController.toggleBoost(player);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(SyncWindStateS2CPayload.TYPE,
                (packet, player, responseSender) -> ClientWindState.update(packet));
    }

    public static void sendToServer(RequestToggleBoostC2SPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static void sendToClient(ServerPlayerEntity player, SyncWindStateS2CPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
