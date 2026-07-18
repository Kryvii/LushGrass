package com.github.kryvii.lushgrass.config;

import com.github.kryvii.lushgrass.LushGrass;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    private static final String TRANSLATION_PREFIX = LushGrass.MOD_ID + ".configuration.visuals";
    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue FULL_GRASS_BLOCK_COVERAGE;
    public static final ModConfigSpec.BooleanValue RENDER_GRASS_TUFTS;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        CLIENT_BUILDER
                .comment("Visual settings.")
                .translation(TRANSLATION_PREFIX);
        CLIENT_BUILDER.push("visuals");
        FULL_GRASS_BLOCK_COVERAGE = CLIENT_BUILDER
                .comment(
                        "Name: Better Grass Blocks",
                        "Description: Improves the appearance of vanilla grass blocks.",
                        "true: enabled; false: disabled."
                )
                .translation(TRANSLATION_PREFIX + ".full_grass_block_coverage")
                .define("full_grass_block_coverage", true);
        RENDER_GRASS_TUFTS = CLIENT_BUILDER
                .comment(
                        "Name: Lusher Grass",
                        "Description: Renders short grass on unobstructed vanilla grass blocks to make grasslands look lusher.",
                        "true: enabled; false: disabled."
                )
                .translation(TRANSLATION_PREFIX + ".render_grass_tufts")
                .define("render_grass_tufts", true);
        CLIENT_BUILDER.pop();

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }

    private ClientConfig() {
    }
}
