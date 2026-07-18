package com.github.kryvii.lushgrass;

import com.github.kryvii.lushgrass.config.ClientConfig;
import com.github.kryvii.lushgrass.init.ModBiomeModifiers;
import com.github.kryvii.lushgrass.init.ModBlocks;
import com.github.kryvii.lushgrass.init.ModCreativeTabs;
import com.github.kryvii.lushgrass.init.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(LushGrass.MOD_ID)
public final class LushGrass {
    public static final String MOD_ID = "lush_grass";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LushGrass(IEventBus modEventBus, ModContainer modContainer) {
        ModBiomeModifiers.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
    }
}
