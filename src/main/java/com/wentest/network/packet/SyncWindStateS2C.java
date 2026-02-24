package com.wentest.network.packet;

import com.wentest.WentestMod;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record SyncWindStateS2C(WindFlightState state, int windPressure, int cooldownTicks, boolean hasFuel) implements FabricPacket {
    public static final PacketType<SyncWindStateS2C> ID = PacketType.create(WentestMod.id("sync_wind_state"));

    public static final PacketType.PacketCodec<PacketByteBuf, SyncWindStateS2C> CODEC =
        PacketType.PacketCodec.of((value, buf) -> {
            buf.writeEnumConstant(value.state);
            buf.writeVarInt(value.windPressure);
            buf.writeVarInt(value.cooldownTicks);
            buf.writeBoolean(value.hasFuel);
        }, buf -> new SyncWindStateS2C(
            buf.readEnumConstant(WindFlightState.class),
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readBoolean()
        ));

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
