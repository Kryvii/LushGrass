package com.github.kryvii.lushgrass.client.mixin.sodium;

import com.github.kryvii.lushgrass.client.compat.sodium.SodiumBlockRenderContextAccess;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext", remap = false)
public abstract class SodiumBlockRenderContextMixin implements SodiumBlockRenderContextAccess {
    @Shadow(remap = false)
    private BakedModel model;

    @Shadow(remap = false)
    private BlockState state;

    @Shadow(remap = false)
    private BlockPos.MutableBlockPos pos;

    @Override
    public BakedModel lushGrass$getModel() {
        return this.model;
    }

    @Override
    public BlockState lushGrass$getState() {
        return this.state;
    }

    @Override
    public BlockPos lushGrass$getPos() {
        return this.pos;
    }
}
