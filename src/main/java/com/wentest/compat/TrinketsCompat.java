package com.wentest.compat;

import com.wentest.WentestMod;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;

public final class TrinketsCompat {
    private TrinketsCompat() {}

    public static boolean hasSkyBeltEquipped(PlayerEntity player) {
        return TrinketsApi.getTrinketComponent(player)
            .map(component -> component.isEquipped(WentestMod.SKY_BELT))
            .orElse(false);
    }
}
