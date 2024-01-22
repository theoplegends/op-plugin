package org.op;

import net.minecraft.world.item.Items;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.utils.InventoryUtils;
import org.rusherhack.core.bind.key.NullKey;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;

import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
public class AutoPearl extends ToggleableModule {

    private final BindSetting rotate = new BindSetting("hotkey", NullKey.INSTANCE /* unbound */);
    private final NumberSetting<Float> rotateYaw = new NumberSetting<>("Yaw", 0f, 0f, 360f).incremental(0.1f);
    private final NumberSetting<Float> rotatePitch = new NumberSetting<>("Pitch", 85f, -90f, 90f).incremental(0.1f);

    public AutoPearl() {
        super("AutoPearl", "Automatically pearl onto block ur facing", ModuleCategory.CLIENT);

        //subsettings
        this.rotate.addSubSettings(this.rotateYaw, this.rotatePitch);

        //register settings
        this.registerSettings(
                this.rotate
        );
    }
    boolean rotating;
    @Subscribe
    private void onUpdate(EventUpdate event) throws InterruptedException {
        if(this.rotate.getValue().isKeyDown()){
            rotating = true;
        }

        //only rotate while bind is held
        if(rotating) {
            //RusherHackAPI.getRotationManager().updateRotation(this.rotateYaw.getValue(), this.rotatePitch.getValue());
            RusherHackAPI.getRotationManager().updateRotation(mc.player.getYRot(), this.rotatePitch.getValue());
            if (RusherHackAPI.getServerState().getPlayerPitch() == 85f) {
                int itemHotbar = InventoryUtils.findItemHotbar(Items.ENDER_PEARL);
                if (itemHotbar >= 0) {
                    mc.getConnection().send(new ServerboundPickItemPacket(itemHotbar));
                    mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 1));
                    mc.getConnection().send(new ServerboundPickItemPacket(itemHotbar));
                    rotating = false;
                }
            }
        }
    }
}