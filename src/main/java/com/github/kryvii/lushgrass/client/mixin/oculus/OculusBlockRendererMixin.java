package com.github.kryvii.lushgrass.client.mixin.oculus;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
public abstract class OculusBlockRendererMixin {
    @Unique
    private static final AtomicBoolean lushGrass$materialWarningLogged = new AtomicBoolean();

    @Unique
    private static Method lushGrass$setMaterialId;

    @Unique
    private Object lushGrass$buffers;

    @Unique
    private BlockState lushGrass$originalState;

    @Unique
    private boolean lushGrass$materialOverridden;

    @Inject(method = "renderModel", at = @At("HEAD"))
    private void lushGrass$beginModel(
            BlockRenderContext context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        BlockState state = context.state();
        if (state != null && state.is(Blocks.GRASS_BLOCK)) {
            this.lushGrass$buffers = buffers;
            this.lushGrass$originalState = state;
        } else {
            this.lushGrass$buffers = null;
            this.lushGrass$originalState = null;
        }
        this.lushGrass$materialOverridden = false;
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
                || !(quad instanceof BakedQuad bakedQuad)
                || bakedQuad.getTintIndex() != GrassBlockTuftModel.TUFT_TINT_INDEX) {
            return;
        }

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
        lushGrass$restoreMaterial();
    }

    @Inject(method = "renderModel", at = @At("RETURN"))
    private void lushGrass$endModel(
            BlockRenderContext context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        lushGrass$restoreMaterial();
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
    private static boolean lushGrass$setMaterial(Object buffers, BlockState state, byte lightEmission) {
        try {
            if (lushGrass$setMaterialId == null) {
                try {
                    lushGrass$setMaterialId = buffers.getClass().getMethod(
                            "iris$setMaterialId",
                            BlockState.class,
                            short.class,
                            byte.class
                    );
                } catch (NoSuchMethodException ignored) {
                    lushGrass$setMaterialId = buffers.getClass().getMethod(
                            "iris$setMaterialId",
                            BlockState.class,
                            short.class
                    );
                }
            }

            if (lushGrass$setMaterialId.getParameterCount() == 3) {
                lushGrass$setMaterialId.invoke(buffers, state, (short) -1, lightEmission);
            } else {
                lushGrass$setMaterialId.invoke(buffers, state, (short) -1);
            }
            return true;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError exception) {
            if (lushGrass$materialWarningLogged.compareAndSet(false, true)) {
                LushGrass.LOGGER.warn(
                        "Could not apply the Oculus short-grass shader material; grass tufts will use the grass-block material.",
                        exception
                );
            }
            return false;
        }
    }
}
