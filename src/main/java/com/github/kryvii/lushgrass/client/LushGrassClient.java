package com.github.kryvii.lushgrass.client;

import com.github.kryvii.lushgrass.client.event.ClientConfigEvents;
import com.github.kryvii.lushgrass.client.event.ClientModelEvents;
import net.minecraftforge.eventbus.api.IEventBus;

public final class LushGrassClient {
    public static void register(IEventBus modEventBus) {
        ClientModelEvents.register(modEventBus);
        ClientConfigEvents.register(modEventBus);
    }

    private LushGrassClient() {
    }
}
