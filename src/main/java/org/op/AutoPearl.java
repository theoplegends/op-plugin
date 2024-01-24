package org.op;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Items;
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

public class AutoPearl extends ToggleableModule {
    
    private final BindSetting rotate = new BindSetting("hotkey", NullKey.INSTANCE /* unbound */);
    private final NumberSetting<Float> rotatePitch = new NumberSetting<>("Pitch", 85f, -90f, 90f).incremental(0.1f);
    
    /**
     * Variables
     */
    private boolean run = false;
    
    public AutoPearl() {
        super("AutoPearl", "Automatically pearl onto block ur facing", ModuleCategory.CLIENT);
        
        //subsettings
        this.rotate.addSubSettings(this.rotatePitch);
        
        //register settings
        this.registerSettings(
                this.rotate
        );
    }
    
    @Subscribe(priority = -1000 /* priority over other rusherhack modules */)
    private void onUpdate(EventUpdate event) {
        
        if(this.run) {
            RusherHackAPI.getRotationManager().updateRotation(mc.player.getYRot(), this.rotatePitch.getValue());
            
            if(RusherHackAPI.getServerState().getPlayerPitch() != this.rotatePitch.getValue()) {
                return;
            }
            
            final int itemHotbar = InventoryUtils.findItemHotbar(Items.ENDER_PEARL);
            
            if(itemHotbar != -1) {
                int oldSlot = -1;
                
                if(itemHotbar != mc.player.getInventory().selected) {
                    oldSlot = mc.player.getInventory().selected;
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected = itemHotbar));
                }
                
                mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 1));
                
                if(oldSlot != -1) {
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected = oldSlot));
                }
            }
            
            this.run = false;
        }
    }
    
    @Subscribe
    private void onKeyPress(EventKeyboard event) {
        if(event.getAction() == GLFW.GLFW_PRESS && this.rotate.getValue() instanceof GLFWKey glfwKey && glfwKey.getKeyCode() == event.getKey()) {
            this.run = true;
        }
    }
    
    @Override
    public void onEnable() {
        this.run = false;
    }
}