package com.github.kryvii.lushgrass.client.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.model.light.data.QuadLightData", remap = false)
public interface SodiumQuadLightDataMixin {
    @Accessor(value = "br", remap = false)
    float[] lushGrass$getBrightness();

    @Accessor(value = "lm", remap = false)
    int[] lushGrass$getLightmap();
}
