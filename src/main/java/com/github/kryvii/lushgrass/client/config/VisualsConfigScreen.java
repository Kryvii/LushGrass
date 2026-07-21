package com.github.kryvii.lushgrass.client.config;

import com.github.kryvii.lushgrass.client.event.ClientConfigEvents;
import com.github.kryvii.lushgrass.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

final class VisualsConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("lush_grass.configuration.visuals");
    private static final Component FULL_COVERAGE =
            Component.translatable("lush_grass.configuration.visuals.full_grass_block_coverage");
    private static final Component FULL_COVERAGE_TOOLTIP =
            Component.translatable("lush_grass.configuration.visuals.full_grass_block_coverage.tooltip");
    private static final Component GRASS_TUFTS =
            Component.translatable("lush_grass.configuration.visuals.render_grass_tufts");
    private static final Component GRASS_TUFTS_TOOLTIP =
            Component.translatable("lush_grass.configuration.visuals.render_grass_tufts.tooltip");

    private final Screen parent;
    private boolean fullCoverage;
    private boolean grassTufts;

    VisualsConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.fullCoverage = ClientConfig.fullGrassBlockCoverage();
        this.grassTufts = ClientConfig.renderGrassTufts();
    }

    @Override
    protected void init() {
        int buttonWidth = Math.min(310, this.width - 40);
        int left = (this.width - buttonWidth) / 2;
        int firstRow = this.height / 2 - 34;

        this.addRenderableWidget(CycleButton.onOffBuilder(this.fullCoverage)
                .withTooltip(value -> Tooltip.create(FULL_COVERAGE_TOOLTIP))
                .create(left, firstRow, buttonWidth, 20, FULL_COVERAGE,
                        (button, value) -> this.fullCoverage = value));
        this.addRenderableWidget(CycleButton.onOffBuilder(this.grassTufts)
                .withTooltip(value -> Tooltip.create(GRASS_TUFTS_TOOLTIP))
                .create(left, firstRow + 24, buttonWidth, 20, GRASS_TUFTS,
                        (button, value) -> this.grassTufts = value));
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .bounds(this.width / 2 - 100, this.height - 28, 200, 20)
                .build());
    }

    @Override
    public void onClose() {
        if (ClientConfig.update(this.fullCoverage, this.grassTufts)) {
            ClientConfig.save();
            ClientConfigEvents.refreshRenderer();
        }

        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
