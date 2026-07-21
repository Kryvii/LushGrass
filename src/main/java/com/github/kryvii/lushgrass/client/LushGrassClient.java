package com.github.kryvii.lushgrass.client;

import com.github.kryvii.lushgrass.client.config.LushGrassConfigScreen;
import com.github.kryvii.lushgrass.client.event.ClientConfigEvents;
import com.github.kryvii.lushgrass.client.event.ClientModelEvents;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;

public final class LushGrassClient {
    public static void register(IEventBus modEventBus) {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new LushGrassConfigScreen(parent)
                )
        );

        ClientModelEvents.register(modEventBus);
        ClientConfigEvents.register(modEventBus);
    }

    private LushGrassClient() {
    }
}
