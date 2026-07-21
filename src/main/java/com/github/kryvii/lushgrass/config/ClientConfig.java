package com.github.kryvii.lushgrass.config;

import com.github.kryvii.lushgrass.LushGrass;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ClientConfig {
    private static final String TRANSLATION_PREFIX = LushGrass.MOD_ID + ".configuration.visuals";
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue FULL_GRASS_BLOCK_COVERAGE;
    public static final ForgeConfigSpec.BooleanValue RENDER_GRASS_TUFTS;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        CLIENT_BUILDER
                .translation(TRANSLATION_PREFIX);
        CLIENT_BUILDER.push("visuals");
        FULL_GRASS_BLOCK_COVERAGE = CLIENT_BUILDER
                .translation(TRANSLATION_PREFIX + ".full_grass_block_coverage")
                .define("full_grass_block_coverage", true);
        RENDER_GRASS_TUFTS = CLIENT_BUILDER
                .translation(TRANSLATION_PREFIX + ".render_grass_tufts")
                .define("render_grass_tufts", true);
        CLIENT_BUILDER.pop();

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }

    private ClientConfig() {
    }
}
