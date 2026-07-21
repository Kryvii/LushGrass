package com.github.kryvii.lushgrass.client.model;

import com.github.kryvii.lushgrass.config.ClientConfig;
import java.util.List;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"deprecation", "removal"})
public class ConfigurableGrassBlockModel implements BakedModel, FabricBakedModel {
    private static final boolean SODIUM_LOADED = FabricLoader.getInstance().isModLoaded("sodium");

    private final BakedModel fullCoverageModel;
    private final BakedModel vanillaModel;

    public ConfigurableGrassBlockModel(BakedModel fullCoverageModel, BakedModel vanillaModel) {
        this.fullCoverageModel = fullCoverageModel;
        this.vanillaModel = vanillaModel;
    }

    protected final BakedModel activeModel() {
        return ClientConfig.fullGrassBlockCoverage() ? this.fullCoverageModel : this.vanillaModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
        return this.activeModel().getQuads(state, side, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.activeModel().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.activeModel().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.activeModel().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.activeModel().isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.activeModel().getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.activeModel().getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.activeModel().getOverrides();
    }

    @Override
    public boolean isVanillaAdapter() {
        return SODIUM_LOADED;
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        emitModel(this.activeModel(), state, context);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(this.activeModel());
    }

    protected static void emitModel(BakedModel model, @Nullable BlockState state, RenderContext context) {
        context.bakedModelConsumer().accept(model, state);
    }
}
