package com.github.kryvii.lushgrass.client.compat.sodium;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface SodiumBlockRenderContextAccess {
    BakedModel lushGrass$getModel();

    BlockState lushGrass$getState();

    BlockPos lushGrass$getPos();
}
