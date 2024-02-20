package org.op;


import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.rusherhack.client.api.events.client.EventUpdate;

import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;

import org.rusherhack.core.event.subscribe.Subscribe;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class JakeOrganCrasher extends ToggleableModule {

    /**
     * Variables
     */

    public JakeOrganCrasher() {
        super("JakeOrganCrash", "FUCK JAKEORGAN, grim.crystalpvp.cc ontop", ModuleCategory.CLIENT);

        //subsettings

        //register settings
        this.registerSettings(
        );
    }

    @Subscribe(priority = -1000 /* priority over other rusherhack modules */)
    private void onUpdate(EventUpdate event) {
        if(mc.player != null) {
            for (int i = 0; i < 16; i++) {
                mc.player.connection.send(new ServerboundContainerClickPacket(mc.player.containerMenu.containerId,
                        mc.player.containerMenu.getStateId(),
                        33,
                        -1,
                        ClickType.SWAP,
                        ItemStack.EMPTY,
                        Int2ObjectMaps.emptyMap()));
            }
        }
    }
}