package com.github.kryvii.lushgrass.datagen;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.init.ModBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public final class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, LushGrass.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.LOW_GRASS.get());
        this.tag(BlockTags.REPLACEABLE).add(ModBlocks.LOW_GRASS.get());
        this.tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.LOW_GRASS.get());
        this.tag(BlockTags.SWORD_EFFICIENT).add(ModBlocks.LOW_GRASS.get());
    }
}
