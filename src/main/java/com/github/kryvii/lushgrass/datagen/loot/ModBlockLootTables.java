package com.github.kryvii.lushgrass.datagen.loot;

import com.github.kryvii.lushgrass.init.ModBlocks;
import java.util.List;
import java.util.Set;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public final class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.LOW_GRASS.get(), this.createGrassDrops(ModBlocks.LOW_GRASS.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModBlocks.LOW_GRASS.get());
    }
}
