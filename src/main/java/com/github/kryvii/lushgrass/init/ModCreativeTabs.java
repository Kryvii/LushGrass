package com.github.kryvii.lushgrass.init;

import com.github.kryvii.lushgrass.LushGrass;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LushGrass.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LUSH_GRASS = CREATIVE_TABS.register(
            "lush_grass",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.lush_grass"))
                    .icon(() -> new ItemStack(Blocks.MOSS_BLOCK))
                    .withTabsBefore(CreativeModeTabs.NATURAL_BLOCKS)
                    .displayItems((parameters, output) -> output.accept(ModItems.LOW_GRASS.get()))
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }

    private ModCreativeTabs() {
    }
}
