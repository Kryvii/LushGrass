package com.github.kryvii.lushgrass.client.mixin.iris;

import com.github.kryvii.lushgrass.client.compat.sodium.QuadTintIndexAccess;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.vertices.sodium.terrain.VertexEncoderInterface;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", remap = false)
public abstract class IrisBlockRendererMixin {
    @Unique
    private int lushGrass$shortGrassMaterialId = -1;

    @Unique
    private boolean lushGrass$renderingGrassBlock;

    @Unique
    private boolean lushGrass$materialOverridden;

    @Inject(method = "prepare", at = @At("HEAD"), require = 0)
    private void lushGrass$resolveShortGrassMaterial(
            @Coerce Object buffers,
            @Coerce Object level,
            @Coerce Object collector,
            CallbackInfo callback
    ) {
        this.lushGrass$shortGrassMaterialId = -1;

        Object2IntMap<BlockState> blockStateIds = WorldRenderingSettings.INSTANCE.getBlockStateIds();
        if (blockStateIds == null) {
            return;
        }

        int materialId = blockStateIds.getInt(Blocks.SHORT_GRASS.defaultBlockState());
        if (materialId != blockStateIds.defaultReturnValue()) {
            this.lushGrass$shortGrassMaterialId = materialId;
        }
    }

    @Inject(method = "renderModel", at = @At("HEAD"), require = 0)
    private void lushGrass$beginModel(
            BakedModel model,
            BlockState state,
            BlockPos pos,
            BlockPos origin,
            CallbackInfo callback
    ) {
        this.lushGrass$renderingGrassBlock = state.is(Blocks.GRASS_BLOCK);
    }

    @Inject(method = "renderModel", at = @At("TAIL"), require = 0)
    private void lushGrass$endModel(
            BakedModel model,
            BlockState state,
            BlockPos pos,
            BlockPos origin,
            CallbackInfo callback
    ) {
        this.lushGrass$renderingGrassBlock = false;
    }

    @Inject(method = "processQuad", at = @At("HEAD"), require = 0)
    private void lushGrass$beforeQuad(@Coerce Object quad, CallbackInfo callback) {
        if (!this.lushGrass$renderingGrassBlock
                || this.lushGrass$shortGrassMaterialId == -1
                || !(quad instanceof QuadTintIndexAccess tintAccess)
                || tintAccess.lushGrass$getTintIndex() != GrassBlockTuftModel.TUFT_TINT_INDEX) {
            return;
        }

        ((VertexEncoderInterface) (Object) this).overrideBlock(this.lushGrass$shortGrassMaterialId);
        this.lushGrass$materialOverridden = true;
    }

    @Inject(method = "processQuad", at = @At("TAIL"), require = 0)
    private void lushGrass$afterQuad(@Coerce Object quad, CallbackInfo callback) {
        if (this.lushGrass$materialOverridden) {
            ((VertexEncoderInterface) (Object) this).restoreBlock();
        }
        this.lushGrass$materialOverridden = false;
    }
}
