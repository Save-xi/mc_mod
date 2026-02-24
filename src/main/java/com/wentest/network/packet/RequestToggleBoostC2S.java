package com.wentest.network.packet;

import com.wentest.WentestMod;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record RequestToggleBoostC2S() implements FabricPacket {
    public static final PacketType<RequestToggleBoostC2S> TYPE = PacketType.create(
        WentestMod.id("request_toggle_boost"),
        RequestToggleBoostC2S::new
    );

    public RequestToggleBoostC2S(PacketByteBuf ignored) {
        this();
    }

    @Override
    public void write(PacketByteBuf buf) {
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
