package org.op;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.render.EventRender3D;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.IRenderer3D;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.utils.ColorUtils;

import java.awt.*;

public class TrapEsp extends ToggleableModule {

    private final ColorSetting espColor = new ColorSetting("Color", Color.RED)
            .setAlphaAllowed(false)
            .setThemeSync(true);

    public TrapEsp() {
        super("TrapEsp", "Shows those annoying 1x1 bedrock holes(DONT USE BROKEN)", ModuleCategory.CLIENT);

        this.registerSettings(
            this.espColor
        );
    }


    BlockPos playerblock;
    Block block;

    @Subscribe
    private void onUpdate(EventUpdate event) {
        if(mc.player != null) {
            playerblock = mc.player.blockPosition();
            //gonna use this later for checking if its air
            //block = mc.level.getBlockState(playerblock).getBlock();
        }
    }

    @Subscribe
    private void onRenderBox(EventRender3D event) {
        final IRenderer3D renderer = event.getRenderer();

        final int color = ColorUtils.transparency(this.espColor.getValueRGB(), 0.5f);

        renderer.begin(event.getMatrixStack());

        if (playerblock != null) {
            renderer.drawBox(playerblock, true, true, color);
        }

        renderer.end();
    }
}
