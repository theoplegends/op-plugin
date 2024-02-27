package org.op;

import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.bind.key.GLFWKey;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.client.input.EventKeyboard;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.bind.key.NullKey;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;

import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import org.rusherhack.core.utils.Timer;


public class HoleEscape extends ToggleableModule {

    private final NumberSetting<Float> rotatePitch = new NumberSetting<>("Pitch", -90f, -90f, 90f).incremental(0.1f);

    private final NumberSetting<Integer> delay = new NumberSetting<>("Delay(ms)", 1, 0, 1000);

    /**
     * Variables
     */

    Timer timer = new Timer();

    public HoleEscape() {
        super("HoleEscape", "Gets you out of 1x1 holes", ModuleCategory.CLIENT);

        //subsettings

        //register settings
        this.registerSettings(
                this.delay,
                this.rotatePitch
        );
    }

    @Subscribe
    private void onUpdate(EventUpdate event) {

        if(timer.passed(this.delay.getValue())){

            RusherHackAPI.getRotationManager().updateRotation(mc.player.getYRot(), this.rotatePitch.getValue());

            if(RusherHackAPI.getServerState().getPlayerPitch() != this.rotatePitch.getValue()) {
                return;
            }

            final int itemHotbar = InventoryUtils.findItemHotbar(Items.ENDER_PEARL);
            if(itemHotbar != -1) {
                int oldSlot = -1;


                //this.getLogger().info("Ticks passed: " + String.valueOf(timer.getTicksPassed()));
                //this.getLogger().info("Time passed(ms): " + String.valueOf(timer.getTime()));
                if (itemHotbar != mc.player.getInventory().selected) {
                    oldSlot = mc.player.getInventory().selected;
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected = itemHotbar));
                }

                mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 1));

                if (oldSlot != -1) {
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected = oldSlot));
                }
                this.toggle();
            }
        }

    }

    //by theoplegends, credits to john, asphyxia and some guy from the rusher discord

    @Override
    public void onEnable() {
        timer.reset();
        mc.player.jumpFromGround();
    }
}