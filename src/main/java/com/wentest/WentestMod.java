package com.wentest;

import com.wentest.mixin.BlockEntityTypeAccessor;
import com.wentest.network.ModNetworking;
import com.wentest.registry.ModBlocks;
import com.wentest.registry.ModItems;
import com.wentest.server.flight.WindFlightController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class WentestMod implements ModInitializer {
    public static final String MOD_ID = "wentest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.register();
        ModBlocks.register();
        ModNetworking.registerC2S();
        patchBrushableSupport();

        ServerTickEvents.END_SERVER_TICK.register(server -> WindFlightController.tickServer(server));
    }

    private static void patchBrushableSupport() {
        Set<Block> patched = new HashSet<>(((BlockEntityTypeAccessor) BlockEntityType.BRUSHABLE_BLOCK).wentest$getBlocks());
        patched.add(ModBlocks.SUSPICIOUS_CLOUD_SAND);
        ((BlockEntityTypeAccessor) BlockEntityType.BRUSHABLE_BLOCK).wentest$setBlocks(patched);
        LOGGER.info("Patched BRUSHABLE_BLOCK supports {} blocks", patched.size());
    }
}
