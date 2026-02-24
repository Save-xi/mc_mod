package com.wentest.network.packet;

import com.wentest.WentestMod;
import com.wentest.server.flight.WindFlightState;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record SyncWindStateS2CPayload(WindFlightState mode, int windPressure, int cooldownTicksRemaining) implements FabricPacket {
    public static final Identifier ID = new Identifier(WentestMod.MOD_ID, "sync_wind_state");
    public static final PacketType<SyncWindStateS2CPayload> TYPE = PacketType.create(ID, SyncWindStateS2CPayload::new);

    public SyncWindStateS2CPayload(PacketByteBuf buf) {
        this(buf.readEnumConstant(WindFlightState.class), buf.readVarInt(), buf.readVarInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(mode);
        buf.writeVarInt(windPressure);
        buf.writeVarInt(cooldownTicksRemaining);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
