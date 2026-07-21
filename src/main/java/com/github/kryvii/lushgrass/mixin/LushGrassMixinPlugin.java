package com.github.kryvii.lushgrass.mixin;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class LushGrassMixinPlugin implements IMixinConfigPlugin {
    private static final String SODIUM_BLOCK_RENDERER =
            "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer";

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !mixinClassName.contains(".sodium.") || hasClassResource(SODIUM_BLOCK_RENDERER);
    }

    private static boolean hasClassResource(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader != null && classLoader.getResource(className.replace('.', '/') + ".class") != null;
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
