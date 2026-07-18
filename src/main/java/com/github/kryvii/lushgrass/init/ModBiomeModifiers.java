package com.github.kryvii.lushgrass.init;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.worldgen.ReplaceFeaturesBiomeModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    private static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, LushGrass.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ReplaceFeaturesBiomeModifier>>
            REPLACE_GRASS_FEATURES = BIOME_MODIFIER_SERIALIZERS.register(
                    "replace_grass_features",
                    () -> ReplaceFeaturesBiomeModifier.CODEC
            );

    public static void register(IEventBus modEventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
    }

    private ModBiomeModifiers() {
    }
}
