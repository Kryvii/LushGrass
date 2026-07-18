package com.github.kryvii.lushgrass.datagen;

import com.github.kryvii.lushgrass.LushGrass;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = LushGrass.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class LushGrassDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        var vanillaPack = generator.getVanillaPack(true);
        vanillaPack.addProvider(packOutput ->
                new ModBlockTagsProvider(packOutput, registries, existingFileHelper));

        generator.addProvider(
                event.includeServer(),
                new ModLootTableProvider(output, registries)
        );
        generator.addProvider(
                event.includeServer(),
                new ModDataMapProvider(output, registries)
        );
    }

    private LushGrassDataGenerators() {
    }
}
