package com.wentest.network.packet;

import com.wentest.WentestMod;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record SyncWindStateS2C(WindFlightState mode, int windPressure, int cooldownTicksRemaining) implements FabricPacket {
    public static final PacketType<SyncWindStateS2C> TYPE = PacketType.create(
        WentestMod.id("sync_wind_state"),
        SyncWindStateS2C::new
    );

    public SyncWindStateS2C(PacketByteBuf buf) {
        this(
            WindFlightState.values()[buf.readByte()],
            buf.readVarInt(),
            buf.readVarInt()
        );
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(mode.ordinal());
        buf.writeVarInt(windPressure);
        buf.writeVarInt(cooldownTicksRemaining);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
