package com.github.kryvii.lushgrass.client.mixin.iris;

import com.github.kryvii.lushgrass.client.compat.sodium.QuadTintIndexAccess;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private static Object lushGrass$worldRenderingSettings;

    @Unique
    private static Method lushGrass$getBlockStateIds;

    @Unique
    private static Method lushGrass$overrideBlock;

    @Unique
    private static Method lushGrass$restoreBlock;

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

        Object2IntMap<BlockState> blockStateIds = lushGrass$getBlockStateIds();
        if (blockStateIds == null) {
            return;
        }

        int materialId = blockStateIds.getInt(Blocks.GRASS.defaultBlockState());
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

        this.lushGrass$materialOverridden = lushGrass$overrideBlock(this, this.lushGrass$shortGrassMaterialId);
    }

    @Inject(method = "processQuad", at = @At("TAIL"), require = 0)
    private void lushGrass$afterQuad(@Coerce Object quad, CallbackInfo callback) {
        if (this.lushGrass$materialOverridden) {
            lushGrass$restoreBlock(this);
        }
        this.lushGrass$materialOverridden = false;
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static Object2IntMap<BlockState> lushGrass$getBlockStateIds() {
        try {
            if (lushGrass$getBlockStateIds == null || lushGrass$worldRenderingSettings == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> settingsClass = Class.forName(
                        "net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings",
                        false,
                        classLoader
                );
                Field instance = settingsClass.getField("INSTANCE");
                lushGrass$worldRenderingSettings = instance.get(null);
                lushGrass$getBlockStateIds = settingsClass.getMethod("getBlockStateIds");
            }

            return (Object2IntMap<BlockState>) lushGrass$getBlockStateIds.invoke(lushGrass$worldRenderingSettings);
        } catch (ReflectiveOperationException | ClassCastException | LinkageError ignored) {
            return null;
        }
    }

    @Unique
    private static boolean lushGrass$overrideBlock(Object renderer, int materialId) {
        try {
            if (lushGrass$overrideBlock == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> vertexEncoder = Class.forName(
                        "net.irisshaders.iris.vertices.sodium.terrain.VertexEncoderInterface",
                        false,
                        classLoader
                );
                lushGrass$overrideBlock = vertexEncoder.getMethod("overrideBlock", int.class);
            }

            lushGrass$overrideBlock.invoke(renderer, materialId);
            return true;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return false;
        }
    }

    @Unique
    private static void lushGrass$restoreBlock(Object renderer) {
        try {
            if (lushGrass$restoreBlock == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> vertexEncoder = Class.forName(
                        "net.irisshaders.iris.vertices.sodium.terrain.VertexEncoderInterface",
                        false,
                        classLoader
                );
                lushGrass$restoreBlock = vertexEncoder.getMethod("restoreBlock");
            }

            lushGrass$restoreBlock.invoke(renderer);
        } catch (ReflectiveOperationException | LinkageError ignored) {
            // Optional Iris compatibility; rendering should continue if this hook is unavailable.
        }
    }
}
