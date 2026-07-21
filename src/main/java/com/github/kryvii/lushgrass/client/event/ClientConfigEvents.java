package com.github.kryvii.lushgrass.client.event;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class ClientConfigEvents {
    private static Boolean lastFullCoverage;
    private static Boolean lastGrassTufts;

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientConfigEvents::onConfigChanged);
    }

    private static void onConfigChanged(ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT
                && LushGrass.MOD_ID.equals(event.getConfig().getModId())) {
            refreshRendererIfChanged();
        }
    }

    public static void refreshRendererIfChanged() {
        boolean fullCoverage = ClientConfig.FULL_GRASS_BLOCK_COVERAGE.get();
        boolean grassTufts = ClientConfig.RENDER_GRASS_TUFTS.get();
        synchronized (ClientConfigEvents.class) {
            if (Boolean.valueOf(fullCoverage).equals(lastFullCoverage)
                    && Boolean.valueOf(grassTufts).equals(lastGrassTufts)) {
                return;
            }
            lastFullCoverage = fullCoverage;
            lastGrassTufts = grassTufts;
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            if (minecraft.levelRenderer != null) {
                minecraft.levelRenderer.allChanged();
            }
        });
    }

    private ClientConfigEvents() {
    }
}
