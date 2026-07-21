package com.github.kryvii.lushgrass.client.event;

import com.github.kryvii.lushgrass.LushGrass;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class ClientConfigEvents {
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientConfigEvents::onConfigChanged);
    }

    private static void onConfigChanged(ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT
                && LushGrass.MOD_ID.equals(event.getConfig().getModId())) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.execute(() -> {
                if (minecraft.levelRenderer != null) {
                    minecraft.levelRenderer.allChanged();
                }
            });
        }
    }

    private ClientConfigEvents() {
    }
}
