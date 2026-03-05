package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public class FullBright extends Hack {
    private final NumberSetting brightness = new NumberSetting("Brightness", "Gamma level for full brightness", 100.0, 1.0, 100.0);
    private double originalGamma = 1.0;

    public FullBright() {
        super("FullBright", HackCategory.RENDER);
        addSetting(brightness);
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null) return;

        if (this.isEnabled()) {
            // Store original gamma on first enable
            if (originalGamma == 1.0) {
                originalGamma = client.options.gamma().get();
            }
            // Set gamma to configured brightness level
            client.options.gamma().set(brightness.getValue());
        } else {
            // Restore original gamma when disabled
            if (originalGamma != 1.0) {
                client.options.gamma().set(originalGamma);
                originalGamma = 1.0;
            }
        }
    }
}