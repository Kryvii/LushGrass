package com.github.kryvii.lushgrass.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class LushGrassMixinPlugin implements IMixinConfigPlugin {
    private Boolean shaderMaterialOverridesAvailable;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.shaderMaterialOverridesAvailable == null) {
            this.shaderMaterialOverridesAvailable = supportsShaderMaterialOverrides();
        }
        return this.shaderMaterialOverridesAvailable;
    }

    private static boolean supportsShaderMaterialOverrides() {
        if (!hasAnyClassResource(
                "net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings",
                "net.coderbot.iris.shaderpack.materialmap.WorldRenderingSettings",
                "net.coderbot.iris.block_rendering.BlockRenderingSettings"
        )) {
            return false;
        }

        for (String className : new String[] {
                "net.irisshaders.iris.vertices.sodium.terrain.VertexEncoderInterface",
                "net.coderbot.iris.vertices.sodium.terrain.VertexEncoderInterface"
        }) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> vertexEncoder = Class.forName(className, false, classLoader);
                vertexEncoder.getMethod("overrideBlock", int.class);
                vertexEncoder.getMethod("restoreBlock");
                return true;
            } catch (ClassNotFoundException | NoSuchMethodException | LinkageError ignored) {
                // Try the next Iris/Oculus package name.
            }
        }

        return false;
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
