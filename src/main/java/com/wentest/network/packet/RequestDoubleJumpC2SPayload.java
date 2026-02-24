package com.wentest.network.packet;

import com.wentest.WentestMod;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record RequestDoubleJumpC2SPayload() implements FabricPacket {
    public static final Identifier ID = new Identifier(WentestMod.MOD_ID, "request_double_jump");
    public static final PacketType<RequestDoubleJumpC2SPayload> TYPE = PacketType.create(ID, RequestDoubleJumpC2SPayload::new);

    public RequestDoubleJumpC2SPayload(PacketByteBuf ignored) {
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
