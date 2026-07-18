package com.github.kryvii.lushgrass.init;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.block.LowGrassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LushGrass.MOD_ID);

    public static final DeferredBlock<LowGrassBlock> LOW_GRASS = BLOCKS.registerBlock(
            "low_grass",
            LowGrassBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)
    );

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    private ModBlocks() {
    }
}
