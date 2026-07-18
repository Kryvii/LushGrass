package com.github.kryvii.lushgrass.client.model;

import com.github.kryvii.lushgrass.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ConfigurableGrassBlockModel extends BakedModelWrapper<BakedModel> {
    private final BakedModel fullCoverageModel;

    public ConfigurableGrassBlockModel(BakedModel fullCoverageModel, BakedModel vanillaModel) {
        super(vanillaModel);
        this.fullCoverageModel = fullCoverageModel;
    }

    protected final BakedModel activeModel() {
        return ClientConfig.FULL_GRASS_BLOCK_COVERAGE.getAsBoolean()
                ? this.fullCoverageModel
                : this.originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return this.activeModel().getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.activeModel().useAmbientOcclusion();
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        return this.activeModel().useAmbientOcclusion(state, data, renderType);
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
    public BakedModel applyTransform(ItemDisplayContext displayContext, PoseStack poseStack, boolean leftHand) {
        return this.activeModel().applyTransform(displayContext, poseStack, leftHand);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        return this.activeModel().getParticleIcon(data);
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource rand,
            ModelData extraData,
            @Nullable RenderType renderType
    ) {
        return this.activeModel().getQuads(state, side, rand, extraData, renderType);
    }

    @Override
    public ModelData getModelData(
            BlockAndTintGetter level,
            BlockPos pos,
            BlockState state,
            ModelData modelData
    ) {
        return this.activeModel().getModelData(level, pos, state, modelData);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return this.activeModel().getRenderTypes(state, rand, data);
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
        return this.activeModel().getRenderTypes(itemStack, fabulous);
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        return this.activeModel().getRenderPasses(itemStack, fabulous);
    }
}
