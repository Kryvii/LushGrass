package com.github.kryvii.lushgrass.worldgen;

import com.github.kryvii.lushgrass.init.ModBiomeModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public final class ReplaceFeaturesBiomeModifier implements BiomeModifier {
    public static final MapCodec<ReplaceFeaturesBiomeModifier> CODEC = Codec
            .unboundedMap(ResourceLocation.CODEC, ConfiguredFeature.CODEC)
            .fieldOf("replacements")
            .xmap(ReplaceFeaturesBiomeModifier::new, modifier -> modifier.replacements);

    private final Map<ResourceLocation, Holder<ConfiguredFeature<?, ?>>> replacements;
    private final Map<ResourceLocation, Holder<PlacedFeature>> replacementCache = new HashMap<>();

    public ReplaceFeaturesBiomeModifier(Map<ResourceLocation, Holder<ConfiguredFeature<?, ?>>> replacements) {
        this.replacements = Map.copyOf(replacements);
    }

    @Override
    public void modify(
            Holder<Biome> biome,
            Phase phase,
            ModifiableBiomeInfo.BiomeInfo.Builder builder
    ) {
        if (phase != Phase.AFTER_EVERYTHING) {
            return;
        }

        for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
            var features = builder.getGenerationSettings().getFeatures(step);
            for (int index = 0; index < features.size(); index++) {
                Holder<PlacedFeature> original = features.get(index);
                var key = original.unwrapKey();
                if (key.isEmpty()) {
                    continue;
                }

                ResourceLocation id = key.get().location();
                Holder<ConfiguredFeature<?, ?>> replacement = this.replacements.get(id);
                if (replacement == null) {
                    continue;
                }

                Holder<PlacedFeature> placedReplacement = this.replacementCache.computeIfAbsent(
                        id,
                        ignored -> Holder.direct(new PlacedFeature(replacement, original.value().placement()))
                );
                features.set(index, placedReplacement);
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.REPLACE_GRASS_FEATURES.get();
    }
}
