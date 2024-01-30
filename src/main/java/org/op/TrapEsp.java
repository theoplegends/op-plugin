package org.op;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.render.EventRender3D;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.IRenderer3D;
import org.rusherhack.core.utils.ColorUtils;
import org.rusherhack.client.api.utils.EntityUtils;
import net.minecraft.world.level.Level;

public class TrapEsp extends ToggleableModule {

    public TrapEsp() {
        super("TrapEsp", "Shows those annoying 1x1 bedrock holes", ModuleCategory.CLIENT);


        //register settings
        this.registerSettings(
        );
    }

    @Subscribe
    private void onUpdate(EventUpdate event) {
        BlockPos pos = new BlockPos(mc.player.getBlockX(),mc.player.getBlockY(), mc.player.getBlockZ());
        Block block = mc.level.getBlockState(pos).getBlock();
        this.getLogger().info(String.valueOf(pos));
    }

    /*@Subscribe
    private void onRenderBox(EventRender3D event) {
        final IRenderer3D renderer = event.getRenderer();

        final int color = ColorUtils.transparency(1, 0.5f);

        renderer.begin(event.getMatrixStack());
        //get positions
        BlockPos pos = null;

        Block block = mc.level.getBlockState(pos).getBlock();

        renderer.end();
    }*/
}
