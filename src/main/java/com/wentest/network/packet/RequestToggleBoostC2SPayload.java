package com.wentest.network.packet;

import com.wentest.WentestMod;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record RequestToggleBoostC2SPayload() implements FabricPacket {
    public static final Identifier ID = new Identifier(WentestMod.MOD_ID, "request_toggle_boost");
    public static final PacketType<RequestToggleBoostC2SPayload> TYPE = PacketType.create(ID, RequestToggleBoostC2SPayload::new);

    public RequestToggleBoostC2SPayload(PacketByteBuf ignored) {
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
