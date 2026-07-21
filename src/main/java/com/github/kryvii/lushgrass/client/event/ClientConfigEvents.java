package com.github.kryvii.lushgrass.client.event;

import net.minecraft.client.Minecraft;

public final class ClientConfigEvents {
    public static void refreshRenderer() {
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
