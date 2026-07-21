package com.github.kryvii.lushgrass.client.event;

import com.github.kryvii.lushgrass.LushGrass;
import com.github.kryvii.lushgrass.client.model.ConfigurableGrassBlockModel;
import com.github.kryvii.lushgrass.client.model.GrassBlockTuftModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientModelEvents {
    private static final ResourceLocation FULL_GRASS_BLOCK_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_block_full");
    private static final ResourceLocation FULL_SNOWY_GRASS_BLOCK_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_block_snow_full");
    private static final ResourceLocation GRASS_TUFT_MODEL =
            new ResourceLocation(LushGrass.MOD_ID, "block/grass_tuft");

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientModelEvents::registerAdditionalModels);
        modEventBus.addListener(EventPriority.LOWEST, ClientModelEvents::modifyBakedModels);
    }

    private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(FULL_GRASS_BLOCK_MODEL);
        event.register(FULL_SNOWY_GRASS_BLOCK_MODEL);
        event.register(GRASS_TUFT_MODEL);
    }

    private static void modifyBakedModels(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation grassBlock = BlockModelShaper.stateToModelLocation(
                Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, false)
        );
        ModelResourceLocation snowyGrassBlock = BlockModelShaper.stateToModelLocation(
                Blocks.GRASS_BLOCK.defaultBlockState().setValue(SnowyDirtBlock.SNOWY, true)
        );
        BakedModel grassModel = event.getModels().get(grassBlock);
        BakedModel snowyGrassModel = event.getModels().get(snowyGrassBlock);
        BakedModel grassTuftModel = event.getModels().get(GRASS_TUFT_MODEL);
        BakedModel fullGrassModel = event.getModels().get(FULL_GRASS_BLOCK_MODEL);
        BakedModel fullSnowyGrassModel = event.getModels().get(FULL_SNOWY_GRASS_BLOCK_MODEL);
        if (grassModel == null
                || snowyGrassModel == null
                || grassTuftModel == null
                || fullGrassModel == null
                || fullSnowyGrassModel == null) {
            LushGrass.LOGGER.warn(
                    "Could not wrap grass block models. grass={}, snowy_grass={}, grass_tuft={}, full_grass={}, full_snowy_grass={}",
                    grassModel != null,
                    snowyGrassModel != null,
                    grassTuftModel != null,
                    fullGrassModel != null,
                    fullSnowyGrassModel != null
            );
            return;
        }

        event.getModels().put(grassBlock, new GrassBlockTuftModel(fullGrassModel, grassModel, grassTuftModel));
        event.getModels().put(
                snowyGrassBlock,
                new ConfigurableGrassBlockModel(fullSnowyGrassModel, snowyGrassModel)
        );
    }

    private ClientModelEvents() {
    }
}
