package com.wentest.registry;

import com.wentest.WentestMod;
import com.wentest.block.SuspiciousCloudSandBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModBlocks {
    public static final Block SUSPICIOUS_CLOUD_SAND = new SuspiciousCloudSandBlock(
            Blocks.SAND,
            AbstractBlock.Settings.copy(Blocks.SUSPICIOUS_SAND)
    );

    private ModBlocks() {}

    public static void register() {
        Registry.register(Registries.BLOCK, WentestMod.id("suspicious_cloud_sand"), SUSPICIOUS_CLOUD_SAND);
        Registry.register(Registries.ITEM, WentestMod.id("suspicious_cloud_sand"), new BlockItem(SUSPICIOUS_CLOUD_SAND, new Item.Settings()));
    }
}
