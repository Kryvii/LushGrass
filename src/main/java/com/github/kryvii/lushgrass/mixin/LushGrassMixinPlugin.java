package com.github.kryvii.lushgrass.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class LushGrassMixinPlugin implements IMixinConfigPlugin {
    private Boolean irisBlockStateMappingsAvailable;
    private Boolean irisSodiumMaterialOverridesAvailable;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("IrisWorldRenderingSettingsMixin")) {
            if (this.irisBlockStateMappingsAvailable == null) {
                this.irisBlockStateMappingsAvailable = supportsIrisBlockStateMappings();
            }
            return this.irisBlockStateMappingsAvailable;
        }

        if (this.irisSodiumMaterialOverridesAvailable == null) {
            this.irisSodiumMaterialOverridesAvailable = supportsIrisSodiumMaterialOverrides();
        }
        return this.irisSodiumMaterialOverridesAvailable;
    }

    private static boolean supportsIrisBlockStateMappings() {
        return hasClassResource("net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings");
    }

    private static boolean supportsIrisSodiumMaterialOverrides() {
        if (!hasClassResource("net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings")) {
            return false;
        }

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> vertexEncoder = Class.forName(
                    "net.irisshaders.iris.vertices.sodium.terrain.VertexEncoderInterface",
                    false,
                    classLoader
            );
            vertexEncoder.getMethod("overrideBlock", int.class);
            vertexEncoder.getMethod("restoreBlock");
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException | LinkageError ignored) {
            return false;
        }
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
