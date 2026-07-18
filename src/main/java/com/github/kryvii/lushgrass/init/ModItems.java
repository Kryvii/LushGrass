package com.github.kryvii.lushgrass.init;

import com.github.kryvii.lushgrass.LushGrass;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LushGrass.MOD_ID);

    public static final DeferredItem<BlockItem> LOW_GRASS =
            ITEMS.registerSimpleBlockItem(ModBlocks.LOW_GRASS, new Item.Properties());

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    private ModItems() {
    }
}
