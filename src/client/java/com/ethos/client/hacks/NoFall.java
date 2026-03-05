package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public class NoFall extends Hack {
    private final NumberSetting minFallDistance = new NumberSetting("Min Fall Distance", "Minimum distance before preventing fall damage", 3.0, 1.0, 10.0);

    public NoFall() {
        super("NoFall", HackCategory.MOVEMENT);
        addSetting(minFallDistance);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null) return;

        // Reset fall distance if it exceeds the minimum threshold
        if (client.player.fallDistance > minFallDistance.getValue()) {
            client.player.fallDistance = 0.0f;
        }
    }
}