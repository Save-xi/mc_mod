package com.wentest.item;

import com.wentest.WentestMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {
    public static final Item SKY_BELT = new Item(new Item.Settings().maxCount(1));

    private ModItems() {
    }

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(WentestMod.MOD_ID, "sky_belt"), SKY_BELT);
    }
}
