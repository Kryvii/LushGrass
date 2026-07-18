package com.github.kryvii.lushgrass.client;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.client.event.ClientColorEvents;
import com.github.kryvii.lushgrass.client.event.ClientConfigEvents;
import com.github.kryvii.lushgrass.client.event.ClientModelEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LushGrass.MOD_ID, dist = Dist.CLIENT)
public final class LushGrassClient {
    public LushGrassClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ClientModelEvents.register(modEventBus);
        ClientColorEvents.register(modEventBus);
        ClientConfigEvents.register(modEventBus);
    }
}
