package com.github.kryvii.lushgrass;

import com.github.kryvii.lushgrass.client.LushGrassClient;
import com.github.kryvii.lushgrass.config.ClientConfig;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LushGrass.MOD_ID)
public final class LushGrass {
    public static final String MOD_ID = "lush_grass";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LushGrass() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LushGrassClient.register(modEventBus));
    }
}
