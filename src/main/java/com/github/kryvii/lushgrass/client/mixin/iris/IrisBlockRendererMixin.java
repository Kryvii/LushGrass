package com.github.kryvii.lushgrass.client.mixin.iris;

import java.lang.reflect.Method;
import net.minecraft.client.renderer.RenderType;
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
public abstract class IrisBlockRendererMixin {
    @Unique
    private static Method lushGrass$contextState;

    @Unique
    private static Method lushGrass$contextRenderLayer;

    @Unique
    private static Method lushGrass$setMaterialId;

    @Unique
    private BlockState lushGrass$originalState;

    @Unique
    private boolean lushGrass$materialOverridden;

    @Inject(method = "renderModel", at = @At("HEAD"), require = 0)
    private void lushGrass$beginModel(
            @Coerce Object context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        this.lushGrass$materialOverridden = false;
        this.lushGrass$originalState = null;

        BlockState state = lushGrass$getState(context);
        RenderType renderLayer = lushGrass$getRenderLayer(context);
        if (state == null || !state.is(Blocks.GRASS_BLOCK) || renderLayer != RenderType.cutout()) {
            return;
        }

        if (lushGrass$setMaterial(buffers, Blocks.GRASS.defaultBlockState(), (byte) 0)) {
            this.lushGrass$originalState = state;
            this.lushGrass$materialOverridden = true;
        }
    }

    @Inject(method = "renderModel", at = @At("RETURN"), require = 0)
    private void lushGrass$endModel(
            @Coerce Object context,
            @Coerce Object buffers,
            CallbackInfo callback
    ) {
        if (this.lushGrass$materialOverridden && this.lushGrass$originalState != null) {
            lushGrass$setMaterial(
                    buffers,
                    this.lushGrass$originalState,
                    (byte) 0
            );
        }
        this.lushGrass$materialOverridden = false;
        this.lushGrass$originalState = null;
    }

    @Unique
    private static BlockState lushGrass$getState(Object context) {
        try {
            if (lushGrass$contextState == null) {
                lushGrass$contextState = context.getClass().getMethod("state");
            }
            return (BlockState) lushGrass$contextState.invoke(context);
        } catch (ReflectiveOperationException | ClassCastException | LinkageError ignored) {
            return null;
        }
    }

    @Unique
    private static RenderType lushGrass$getRenderLayer(Object context) {
        try {
            if (lushGrass$contextRenderLayer == null) {
                lushGrass$contextRenderLayer = context.getClass().getMethod("renderLayer");
            }
            return (RenderType) lushGrass$contextRenderLayer.invoke(context);
        } catch (ReflectiveOperationException | ClassCastException | LinkageError ignored) {
            return null;
        }
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
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return false;
        }
    }
}
