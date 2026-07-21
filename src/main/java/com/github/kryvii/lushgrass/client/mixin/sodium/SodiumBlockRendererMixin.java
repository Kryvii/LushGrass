package com.github.kryvii.lushgrass.client.mixin.sodium;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.client.compat.sodium.SodiumBlockRenderContextAccess;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.level.BlockAndTintGetter;
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
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", remap = false)
public abstract class SodiumBlockRendererMixin {
    @Unique
    private static final AtomicBoolean lushGrass$materialWarningLogged = new AtomicBoolean();

    @Unique
    private static final AtomicBoolean lushGrass$sodiumPathLogged = new AtomicBoolean();

    @Unique
    private static final AtomicBoolean lushGrass$irisPathLogged = new AtomicBoolean();

    @Unique
    private static final boolean lushGrass$irisMaterialApiAvailable = lushGrass$hasIrisMaterialApi();

    @Unique
    private static Method lushGrass$getWorld;

    @Unique
    private static Method lushGrass$setMaterialId;

    @Unique
    private static Method lushGrass$getColorIndex;

    @Unique
    private GrassBlockTuftModel lushGrass$activeModel;

    @Unique
    private Object lushGrass$buffers;

    @Unique
    private BlockState lushGrass$originalState;

    @Unique
    private boolean lushGrass$materialOverridden;

    @Inject(method = "renderModel", at = @At("HEAD"))
    private void lushGrass$beginModel(
            @Coerce Object context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        this.lushGrass$activeModel = null;
        this.lushGrass$buffers = null;
        this.lushGrass$originalState = null;
        this.lushGrass$materialOverridden = false;
        if (!(context instanceof SodiumBlockRenderContextAccess access)
                || !(access.lushGrass$getModel() instanceof GrassBlockTuftModel grassModel)) {
            return;
        }

        BlockAndTintGetter world = lushGrass$getWorld(context);
        if (world == null) {
            return;
        }

        this.lushGrass$activeModel = grassModel;
        this.lushGrass$buffers = buffers;
        this.lushGrass$originalState = access.lushGrass$getState();
        grassModel.beginSodiumRender(world, this.lushGrass$originalState, access.lushGrass$getPos());
        if (lushGrass$sodiumPathLogged.compareAndSet(false, true)) {
            LushGrass.LOGGER.info("Enabled direct Sodium grass-tuft rendering without requiring Indium.");
        }
    }

    @Inject(method = "writeGeometry", at = @At("HEAD"))
    private void lushGrass$beginQuad(
            @Coerce Object context,
            @Coerce Object builder,
            @Coerce Object offset,
            @Coerce Object material,
            @Coerce Object quad,
            int[] colors,
            @Coerce Object light,
            CallbackInfo callback
    ) {
        this.lushGrass$materialOverridden = false;
        if (this.lushGrass$buffers == null
                || lushGrass$getTintIndex(quad) != GrassBlockTuftModel.TUFT_TINT_INDEX) {
            return;
        }
        lushGrass$applyTuftLight(light, quad);
        this.lushGrass$materialOverridden = lushGrass$setMaterial(
                this.lushGrass$buffers,
                Blocks.GRASS.defaultBlockState(),
                (byte) 0
        );
    }

    @Inject(method = "writeGeometry", at = @At("RETURN"))
    private void lushGrass$endQuad(
            @Coerce Object context,
            @Coerce Object builder,
            @Coerce Object offset,
            @Coerce Object material,
            @Coerce Object quad,
            int[] colors,
            @Coerce Object light,
            CallbackInfo callback
    ) {
        this.lushGrass$restoreMaterial();
    }

    @Inject(method = "renderModel", at = @At("RETURN"))
    private void lushGrass$endModel(
            @Coerce Object context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        this.lushGrass$restoreMaterial();
        if (this.lushGrass$activeModel != null) {
            this.lushGrass$activeModel.endSodiumRender();
        }
        this.lushGrass$activeModel = null;
        this.lushGrass$buffers = null;
        this.lushGrass$originalState = null;
    }

    @Unique
    private void lushGrass$restoreMaterial() {
        if (this.lushGrass$materialOverridden
                && this.lushGrass$buffers != null
                && this.lushGrass$originalState != null) {
            lushGrass$setMaterial(this.lushGrass$buffers, this.lushGrass$originalState, (byte) 0);
        }
        this.lushGrass$materialOverridden = false;
    }

    @Unique
    private static BlockAndTintGetter lushGrass$getWorld(Object context) {
        try {
            if (lushGrass$getWorld == null) {
                lushGrass$getWorld = context.getClass().getMethod("world");
            }
            Object world = lushGrass$getWorld.invoke(context);
            return world instanceof BlockAndTintGetter blockView ? blockView : null;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError exception) {
            return null;
        }
    }

    @Unique
    private static int lushGrass$getTintIndex(Object quad) {
        if (quad instanceof BakedQuad bakedQuad) {
            return bakedQuad.getTintIndex();
        }
        try {
            if (lushGrass$getColorIndex == null
                    || !lushGrass$getColorIndex.getDeclaringClass().isInstance(quad)) {
                lushGrass$getColorIndex = quad.getClass().getMethod("getColorIndex");
            }
            return (int) lushGrass$getColorIndex.invoke(quad);
        } catch (ReflectiveOperationException | RuntimeException | LinkageError exception) {
            return -1;
        }
    }

    @Unique
    private static void lushGrass$applyTuftLight(Object light, Object quad) {
        if (!(light instanceof SodiumQuadLightDataMixin lightData)
                || !(quad instanceof BakedQuad bakedQuad)) {
            return;
        }
        int[] vertices = bakedQuad.getVertices();
        int stride = vertices.length / 4;
        if (stride <= 6) {
            return;
        }
        Arrays.fill(lightData.lushGrass$getBrightness(), 1.0F);
        Arrays.fill(lightData.lushGrass$getLightmap(), vertices[6]);
    }

    @Unique
    private static boolean lushGrass$setMaterial(Object buffers, BlockState state, byte lightEmission) {
        if (!lushGrass$irisMaterialApiAvailable) {
            return false;
        }
        try {
            if (lushGrass$setMaterialId == null) {
                lushGrass$setMaterialId = buffers.getClass().getMethod(
                        "iris$setMaterialId",
                        BlockState.class,
                        short.class,
                        byte.class
                );
            }
            lushGrass$setMaterialId.invoke(buffers, state, (short) -1, lightEmission);
            if (lushGrass$irisPathLogged.compareAndSet(false, true)) {
                LushGrass.LOGGER.info("Applied the Iris short-grass shader material to grass tufts.");
            }
            return true;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError exception) {
            if (lushGrass$materialWarningLogged.compareAndSet(false, true)) {
                LushGrass.LOGGER.warn(
                        "Could not apply the Iris short-grass shader material; grass tufts will use the grass-block material.",
                        exception
                );
            }
            return false;
        }
    }

    @Unique
    private static boolean lushGrass$hasIrisMaterialApi() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader != null
                && classLoader.getResource(
                        "net/irisshaders/iris/compat/sodium/impl/block_context/ChunkBuildBuffersExt.class"
                ) != null;
    }
}
