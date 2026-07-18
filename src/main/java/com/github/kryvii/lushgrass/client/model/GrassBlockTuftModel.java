package com.github.kryvii.lushgrass.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.github.kryvii.lushgrass.config.ClientConfig;
import com.google.common.cache.CacheBuilder;
import com.mojang.math.Transformation;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public final class GrassBlockTuftModel extends ConfigurableGrassBlockModel {
    public static final int TUFT_TINT_INDEX = 0x4C47;

    private static final int MAX_TUFT_VARIANTS = 8192;
    private static final ModelProperty<TuftVariant> TUFT_DATA = new ModelProperty<>(data -> data != null);
    private static final BlockState GRASS_BLOCK_STATE = Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, false);
    private static final BlockState SHORT_GRASS_STATE = Blocks.SHORT_GRASS.defaultBlockState();

    private final List<BakedQuad> tuftTemplateQuads;
    private final ChunkRenderTypeSet fullCoverageRenderTypes;
    private final ChunkRenderTypeSet vanillaRenderTypes;
    private final ChunkRenderTypeSet tuftRenderTypes;
    private final ChunkRenderTypeSet combinedFullCoverageRenderTypes;
    private final ChunkRenderTypeSet combinedVanillaRenderTypes;
    private final ConcurrentMap<TuftVariantKey, TuftVariant> tuftVariantCache = CacheBuilder.newBuilder()
            .maximumSize(MAX_TUFT_VARIANTS)
            .<TuftVariantKey, TuftVariant>build()
            .asMap();

    public GrassBlockTuftModel(
            BakedModel fullCoverageModel,
            BakedModel vanillaModel,
            BakedModel shortGrassModel
    ) {
        super(fullCoverageModel, vanillaModel);
        this.tuftTemplateQuads = shortGrassModel
                .getQuads(SHORT_GRASS_STATE, null, RandomSource.create(42L), ModelData.EMPTY, null)
                .stream()
                .map(GrassBlockTuftModel::markTuftQuad)
                .toList();
        this.fullCoverageRenderTypes = fullCoverageModel.getRenderTypes(
                GRASS_BLOCK_STATE,
                RandomSource.create(42L),
                ModelData.EMPTY
        );
        this.vanillaRenderTypes = vanillaModel.getRenderTypes(
                GRASS_BLOCK_STATE,
                RandomSource.create(42L),
                ModelData.EMPTY
        );
        this.tuftRenderTypes = shortGrassModel.getRenderTypes(SHORT_GRASS_STATE, RandomSource.create(42L), ModelData.EMPTY);
        this.combinedFullCoverageRenderTypes =
                ChunkRenderTypeSet.union(this.fullCoverageRenderTypes, this.tuftRenderTypes);
        this.combinedVanillaRenderTypes =
                ChunkRenderTypeSet.union(this.vanillaRenderTypes, this.tuftRenderTypes);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        ModelData originalData = this.activeModel().getModelData(level, pos, state, modelData);
        if (!ClientConfig.RENDER_GRASS_TUFTS.getAsBoolean()
                || state == null
                || !state.is(Blocks.GRASS_BLOCK)
                || state.getValue(SnowyDirtBlock.SNOWY)) {
            return originalData;
        }

        BlockPos tuftPos = pos.above();
        BlockState aboveState = level.getBlockState(tuftPos);
        if (!aboveState.isAir() || !aboveState.getFluidState().isEmpty() || aboveState.is(BlockTags.SNOW)) {
            return originalData;
        }

        Vec3 offset = SHORT_GRASS_STATE.getOffset(level, tuftPos);
        int packedLight = LevelRenderer.getLightColor(level, aboveState, tuftPos);
        TuftVariantKey key = new TuftVariantKey(offset, packedLight);
        TuftVariant variant = this.tuftVariantCache.computeIfAbsent(key, this::createTuftVariant);
        return originalData == ModelData.EMPTY
                ? variant.modelData()
                : originalData.derive().with(TUFT_DATA, variant).build();
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        if (ClientConfig.FULL_GRASS_BLOCK_COVERAGE.getAsBoolean()) {
            return ClientConfig.RENDER_GRASS_TUFTS.getAsBoolean() && data.has(TUFT_DATA)
                    ? this.combinedFullCoverageRenderTypes
                    : this.fullCoverageRenderTypes;
        }

        return ClientConfig.RENDER_GRASS_TUFTS.getAsBoolean() && data.has(TUFT_DATA)
                ? this.combinedVanillaRenderTypes
                : this.vanillaRenderTypes;
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        ChunkRenderTypeSet baseRenderTypes = ClientConfig.FULL_GRASS_BLOCK_COVERAGE.getAsBoolean()
                ? this.fullCoverageRenderTypes
                : this.vanillaRenderTypes;
        if (renderType != null && this.tuftRenderTypes.contains(renderType) && !baseRenderTypes.contains(renderType)) {
            return TriState.FALSE;
        }

        return this.activeModel().useAmbientOcclusion(state, data, renderType);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        ChunkRenderTypeSet baseRenderTypes = ClientConfig.FULL_GRASS_BLOCK_COVERAGE.getAsBoolean()
                ? this.fullCoverageRenderTypes
                : this.vanillaRenderTypes;
        List<BakedQuad> baseQuads = renderType == null || baseRenderTypes.contains(renderType)
                ? this.activeModel().getQuads(state, side, rand, extraData, renderType)
                : List.of();
        TuftVariant variant = extraData.get(TUFT_DATA);
        if (side != null
                || variant == null
                || !ClientConfig.RENDER_GRASS_TUFTS.getAsBoolean()
                || renderType != null && !this.tuftRenderTypes.contains(renderType)) {
            return baseQuads;
        }

        List<BakedQuad> tuftQuads = variant.quads();
        if (baseQuads.isEmpty()) {
            return tuftQuads;
        }
        if (tuftQuads.isEmpty()) {
            return baseQuads;
        }

        List<BakedQuad> combined = new ArrayList<>(baseQuads.size() + tuftQuads.size());
        combined.addAll(baseQuads);
        combined.addAll(tuftQuads);
        return combined;
    }

    private TuftVariant createTuftVariant(TuftVariantKey key) {
        Vec3 offset = key.offset();
        Transformation translation = new Transformation(
                new Vector3f((float)offset.x, (float)(1.0D + offset.y), (float)offset.z),
                null,
                new Vector3f(1.0F, 0.4375F, 1.0F),
                null
        );
        List<BakedQuad> quads = List.copyOf(
                QuadTransformers.applying(translation)
                        .andThen(QuadTransformers.applyingLightmap(key.packedLight()))
                        .process(this.tuftTemplateQuads)
        );
        return new TuftVariant(quads);
    }

    private static BakedQuad markTuftQuad(BakedQuad quad) {
        return new BakedQuad(
                quad.getVertices().clone(),
                TUFT_TINT_INDEX,
                quad.getDirection(),
                quad.getSprite(),
                quad.isShade(),
                quad.hasAmbientOcclusion()
        );
    }

    private record TuftVariantKey(Vec3 offset, int packedLight) {
    }

    private static final class TuftVariant {
        private final List<BakedQuad> quads;
        private final ModelData modelData;

        private TuftVariant(List<BakedQuad> quads) {
            this.quads = quads;
            this.modelData = ModelData.of(TUFT_DATA, this);
        }

        private List<BakedQuad> quads() {
            return this.quads;
        }

        private ModelData modelData() {
            return this.modelData;
        }
    }
}
