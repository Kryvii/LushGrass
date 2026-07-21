package com.github.kryvii.lushgrass.client.event;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.client.model.ConfigurableGrassBlockModel;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import org.jetbrains.annotations.Nullable;

public final class ClientModelEvents {
    private static final ResourceLocation FULL_GRASS_BLOCK_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_block_full");
    private static final ResourceLocation FULL_SNOWY_GRASS_BLOCK_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_block_snow_full");
    private static final ResourceLocation GRASS_TUFT_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_tuft");
    private static final ModelResourceLocation GRASS_BLOCK_MODEL = BlockModelShaper.stateToModelLocation(
            Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, false)
    );
    private static final ModelResourceLocation SNOWY_GRASS_BLOCK_MODEL = BlockModelShaper.stateToModelLocation(
            Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true)
    );

    public static void register() {
        ModelLoadingPlugin.register(context -> {
            context.addModels(FULL_GRASS_BLOCK_MODEL, FULL_SNOWY_GRASS_BLOCK_MODEL, GRASS_TUFT_MODEL);
            context.modifyModelAfterBake().register(ClientModelEvents::modifyBakedModel);
        });
    }

    private static @Nullable BakedModel modifyBakedModel(
            @Nullable BakedModel model,
            ModelModifier.AfterBake.Context context
    ) {
        if (model == null) {
            return null;
        }

        ResourceLocation id = context.id();
        if (GRASS_BLOCK_MODEL.equals(id)) {
            BakedModel fullGrassModel = context.baker().bake(FULL_GRASS_BLOCK_MODEL, context.settings());
            BakedModel grassTuftModel = context.baker().bake(GRASS_TUFT_MODEL, context.settings());
            if (fullGrassModel == null || grassTuftModel == null) {
                LushGrass.LOGGER.warn(
                        "Could not wrap grass block model. full_grass={}, grass_tuft={}",
                        fullGrassModel != null,
                        grassTuftModel != null
                );
                return model;
            }
            LushGrass.LOGGER.info("Wrapped the vanilla grass block model with Lush Grass visuals.");
            return new GrassBlockTuftModel(fullGrassModel, model, grassTuftModel);
        }

        if (SNOWY_GRASS_BLOCK_MODEL.equals(id)) {
            BakedModel fullSnowyGrassModel = context.baker().bake(FULL_SNOWY_GRASS_BLOCK_MODEL, context.settings());
            if (fullSnowyGrassModel == null) {
                LushGrass.LOGGER.warn("Could not wrap snowy grass block model. full_snowy_grass=false");
                return model;
            }
            return new ConfigurableGrassBlockModel(fullSnowyGrassModel, model);
        }

        return model;
    }

    private ClientModelEvents() {
    }
}
