package com.github.kryvii.lushgrass.client.mixin.iris;

import com.github.kryvii.lushgrass.init.ModBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings", remap = false)
public abstract class IrisWorldRenderingSettingsMixin {
    @Inject(method = "setBlockStateIds", at = @At("RETURN"), require = 0, remap = false)
    private void lushGrass$inheritShortGrassMaterial(
            Object2IntMap<BlockState> blockStateIds,
            CallbackInfo callback
    ) {
        BlockState shortGrass = Blocks.SHORT_GRASS.defaultBlockState();
        if (!blockStateIds.containsKey(shortGrass)) {
            return;
        }

        int materialId = blockStateIds.getInt(shortGrass);
        for (BlockState state : ModBlocks.LOW_GRASS.get().getStateDefinition().getPossibleStates()) {
            blockStateIds.putIfAbsent(state, materialId);
        }
    }
}
