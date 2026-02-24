package com.wentest.registry;

import com.wentest.WentestMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModItems {
    public static final Item SKY_BELT = Registry.register(
        Registries.ITEM,
        WentestMod.id("sky_belt"),
        new Item(new Item.Settings().maxCount(1))
    );

    private ModItems() {
    }

    public static void register() {
        // Static init hook
    }
}
