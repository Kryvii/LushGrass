package com.github.kryvii.lushgrass.client.mixin.sodium;

import com.github.kryvii.lushgrass.client.compat.sodium.QuadTintIndexAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.frapi.mesh.QuadViewImpl", remap = false)
public abstract class SodiumQuadViewMixin implements QuadTintIndexAccess {
    @Shadow
    public abstract int colorIndex();

    @Override
    public int lushGrass$getTintIndex() {
        return this.colorIndex();
    }
}
