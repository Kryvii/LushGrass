package com.github.kryvii.lushgrass.client.model;

import com.github.kryvii.lushgrass.config.ClientConfig;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class GrassBlockTuftModel extends ConfigurableGrassBlockModel {
    public static final int TUFT_TINT_INDEX = 0x4C47;

    private static final int TUFT_OFFSET_SEED_X = 17;
    private static final int TUFT_OFFSET_SEED_Z = 31;
    private static final float TUFT_HEIGHT_SCALE = 0.4375F;
    private static final int MAX_TUFT_VARIANTS = 8192;
    private static final BlockState SHORT_GRASS_STATE = Blocks.GRASS.defaultBlockState();

    private final BakedModel tuftModel;
    private final List<BakedQuad> tuftTemplateQuads;
    private final ConcurrentMap<TuftVariantKey, List<BakedQuad>> tuftVariantCache = CacheBuilder.newBuilder()
            .maximumSize(MAX_TUFT_VARIANTS)
            .<TuftVariantKey, List<BakedQuad>>build()
            .asMap();
    private final ThreadLocal<SodiumRenderContext> sodiumRenderContext = new ThreadLocal<>();

    public GrassBlockTuftModel(BakedModel fullCoverageModel, BakedModel vanillaModel, BakedModel tuftModel) {
        super(fullCoverageModel, vanillaModel);
        this.tuftModel = tuftModel;
        this.tuftTemplateQuads = List.copyOf(
                tuftModel.getQuads(SHORT_GRASS_STATE, null, RandomSource.create(42L))
        );
    }

    @Override
    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource random
    ) {
        List<BakedQuad> baseQuads = super.getQuads(state, side, random);
        SodiumRenderContext context = this.sodiumRenderContext.get();
        if (side != null || context == null || !shouldRenderTuft(context.blockView(), context.state(), context.pos())) {
            return baseQuads;
        }

        List<BakedQuad> tuftQuads = this.getTuftQuads(context.blockView(), context.pos());
        if (baseQuads.isEmpty()) {
            return tuftQuads;
        }
        List<BakedQuad> combined = new ArrayList<>(baseQuads.size() + tuftQuads.size());
        combined.addAll(baseQuads);
        combined.addAll(tuftQuads);
        return combined;
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
        if (!shouldRenderTuft(blockView, state, pos)) {
            return;
        }

        BlockPos tuftPos = pos.above();
        BlockState aboveState = blockView.getBlockState(tuftPos);
        BlockPos offsetSeedPos = tuftPos.offset(TUFT_OFFSET_SEED_X, 0, TUFT_OFFSET_SEED_Z);
        Vec3 offset = SHORT_GRASS_STATE.getOffset(blockView, offsetSeedPos);
        int packedLight = LevelRenderer.getLightColor(blockView, aboveState, tuftPos);

        context.pushTransform(quad -> transformTuftQuad(quad, offset, packedLight));
        try {
            emitModel(this.tuftModel, SHORT_GRASS_STATE, context);
        } finally {
            context.popTransform();
        }
    }

    private static boolean shouldRenderTuft(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
        if (!ClientConfig.renderGrassTufts()
                || !state.is(Blocks.GRASS_BLOCK)
                || state.getValue(SnowyDirtBlock.SNOWY)) {
            return false;
        }

        BlockPos tuftPos = pos.above();
        BlockState aboveState = blockView.getBlockState(tuftPos);
        return !Block.isShapeFullBlock(aboveState.getCollisionShape(blockView, tuftPos));
    }

    private static boolean transformTuftQuad(MutableQuadView quad, Vec3 offset, int packedLight) {
        for (int vertex = 0; vertex < 4; vertex++) {
            quad.pos(
                    vertex,
                    quad.x(vertex) + (float) offset.x,
                    quad.y(vertex) * TUFT_HEIGHT_SCALE + (float) (1.0D + offset.y),
                    quad.z(vertex) + (float) offset.z
            );
            quad.lightmap(vertex, packedLight);
        }
        quad.colorIndex(TUFT_TINT_INDEX);
        return true;
    }

    public void beginSodiumRender(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
        this.sodiumRenderContext.set(new SodiumRenderContext(blockView, state, pos.immutable()));
    }

    public void endSodiumRender() {
        this.sodiumRenderContext.remove();
    }

    private List<BakedQuad> getTuftQuads(BlockAndTintGetter blockView, BlockPos pos) {
        BlockPos tuftPos = pos.above();
        BlockState aboveState = blockView.getBlockState(tuftPos);
        BlockPos offsetSeedPos = tuftPos.offset(TUFT_OFFSET_SEED_X, 0, TUFT_OFFSET_SEED_Z);
        Vec3 offset = SHORT_GRASS_STATE.getOffset(blockView, offsetSeedPos);
        int packedLight = LevelRenderer.getLightColor(blockView, aboveState, tuftPos);
        return this.tuftVariantCache.computeIfAbsent(
                new TuftVariantKey(offset, packedLight),
                this::createTuftVariant
        );
    }

    private List<BakedQuad> createTuftVariant(TuftVariantKey key) {
        List<BakedQuad> quads = new ArrayList<>(this.tuftTemplateQuads.size());
        for (BakedQuad quad : this.tuftTemplateQuads) {
            int[] vertices = quad.getVertices().clone();
            int stride = vertices.length / 4;
            for (int vertex = 0; vertex < 4; vertex++) {
                int offset = vertex * stride;
                float x = Float.intBitsToFloat(vertices[offset]);
                float y = Float.intBitsToFloat(vertices[offset + 1]);
                float z = Float.intBitsToFloat(vertices[offset + 2]);
                vertices[offset] = Float.floatToRawIntBits(x + (float) key.offset().x);
                vertices[offset + 1] = Float.floatToRawIntBits(
                        y * TUFT_HEIGHT_SCALE + (float) (1.0D + key.offset().y)
                );
                vertices[offset + 2] = Float.floatToRawIntBits(z + (float) key.offset().z);
                if (stride > 6) {
                    vertices[offset + 6] = key.packedLight();
                }
            }
            quads.add(new BakedQuad(
                    vertices,
                    TUFT_TINT_INDEX,
                    quad.getDirection(),
                    quad.getSprite(),
                    quad.isShade()
            ));
        }
        return List.copyOf(quads);
    }

    private record SodiumRenderContext(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
    }

    private record TuftVariantKey(Vec3 offset, int packedLight) {
    }
}
