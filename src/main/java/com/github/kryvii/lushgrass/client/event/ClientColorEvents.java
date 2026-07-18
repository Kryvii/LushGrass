package com.github.kryvii.lushgrass.client.event;

import com.github.kryvii.lushgrass.init.ModBlocks;
import com.github.kryvii.lushgrass.init.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class ClientColorEvents {
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientColorEvents::registerBlockColors);
        modEventBus.addListener(ClientColorEvents::registerItemColors);
    }

    private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) -> event.getBlockColors().getColor(
                        Blocks.SHORT_GRASS.defaultBlockState(),
                        level,
                        pos,
                        tintIndex
                ),
                ModBlocks.LOW_GRASS.get()
        );
    }

    private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> event.getBlockColors().getColor(
                        ModBlocks.LOW_GRASS.get().defaultBlockState(),
                        null,
                        null,
                        tintIndex
                ),
                ModItems.LOW_GRASS.get()
        );
    }

    private ClientColorEvents() {
    }
}
