package com.wentest;

import com.wentest.network.packet.RequestToggleBoostC2S;
import com.wentest.network.packet.SyncWindStateS2C;
import com.wentest.server.flight.WindFlightController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WentestMod implements ModInitializer {
    public static final String MOD_ID = "wentest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Item SKY_BELT = registerItem("sky_belt", new Item(new Item.Settings().maxCount(1)));

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(RequestToggleBoostC2S.ID, RequestToggleBoostC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncWindStateS2C.ID, SyncWindStateS2C.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestToggleBoostC2S.ID,
            (payload, context) -> WindFlightController.handleToggleRequest(context.player()));

        WindFlightController.registerTickHandler();
        LOGGER.info("Echoes of the Sky initialized");
    }

    public static boolean isWindFuel(Item item) {
        return item == Items.PRISMARINE_CRYSTALS;
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    private static Item registerItem(String path, Item item) {
        return Registry.register(Registries.ITEM, id(path), item);
    }
}
