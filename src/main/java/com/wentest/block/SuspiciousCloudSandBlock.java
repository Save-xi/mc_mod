package com.wentest.block;

import com.wentest.WentestMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SuspiciousCloudSandBlock extends BrushableBlock {
    public SuspiciousCloudSandBlock(Block baseBlock, Settings settings) {
        super(baseBlock, settings, SoundEvents.ITEM_BRUSH_BRUSHING_SAND, SoundEvents.ITEM_BRUSH_BRUSHING_SAND_COMPLETE);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient) {
            return;
        }
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BrushableBlockEntity brushable) {
            brushable.setLootTable(WentestMod.id("archaeology/suspicious_cloud_sand"), world.random.nextLong());
        }
    }
}
