package com.wentest.registry;

import com.wentest.WentestMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModItems {
    public static final Item SKY_BELT = new Item(new Item.Settings().maxCount(1));
    public static final Item WIND_CRYSTAL = new Item(new Item.Settings());
    public static final Item GALE_CORE_SHARD = new Item(new Item.Settings());

    private ModItems() {}

    public static void register() {
        Registry.register(Registries.ITEM, WentestMod.id("sky_belt"), SKY_BELT);
        Registry.register(Registries.ITEM, WentestMod.id("wind_crystal"), WIND_CRYSTAL);
        Registry.register(Registries.ITEM, WentestMod.id("gale_core_shard"), GALE_CORE_SHARD);
    }
}
