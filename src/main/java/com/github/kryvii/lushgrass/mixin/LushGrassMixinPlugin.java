package com.github.kryvii.lushgrass.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class LushGrassMixinPlugin implements IMixinConfigPlugin {
    private Boolean oculusMaterialOverridesAvailable;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.oculusMaterialOverridesAvailable == null) {
            this.oculusMaterialOverridesAvailable = supportsOculusMaterialOverrides();
        }
        return this.oculusMaterialOverridesAvailable;
    }

    private static boolean supportsOculusMaterialOverrides() {
        return hasClassResource("me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer")
                && hasAnyClassResource(
                        "net.irisshaders.iris.compat.sodium.impl.block_context.ChunkBuildBuffersExt",
                        "net.coderbot.iris.compat.sodium.impl.block_context.ChunkBuildBuffersExt"
                );
    }

    private static boolean hasClassResource(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader != null && classLoader.getResource(className.replace('.', '/') + ".class") != null;
    }

    private static boolean hasAnyClassResource(String... classNames) {
        for (String className : classNames) {
            if (hasClassResource(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
