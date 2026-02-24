package com.wentest.network.packet;

import com.wentest.WentestMod;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record RequestToggleBoostC2S() implements FabricPacket {
    public static final PacketType<RequestToggleBoostC2S> ID = PacketType.create(WentestMod.id("request_toggle_boost"));

    public static final PacketType.PacketCodec<PacketByteBuf, RequestToggleBoostC2S> CODEC =
        PacketType.PacketCodec.of((value, buf) -> {
        }, buf -> new RequestToggleBoostC2S());

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
