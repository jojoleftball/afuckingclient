package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public class Flight extends Hack {
    private final NumberSetting flySpeed = new NumberSetting("Fly Speed", "Flying speed multiplier", 1.0, 0.1, 5.0);

    public Flight() {
        super("Flight", HackCategory.MOVEMENT);
        addSetting(flySpeed);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null) return;

        // Enable flying ability
        client.player.getAbilities().mayfly = true;
        client.player.getAbilities().flying = true;

        // Update abilities on client
        client.player.onUpdateAbilities();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        // When disabled, restore normal flight abilities
        if (!enabled && Minecraft.getInstance().player != null) {
            Minecraft client = Minecraft.getInstance();
            client.player.getAbilities().mayfly = client.player.getAbilities().instabuild; // Only allow flying in creative
            client.player.getAbilities().flying = false;
            client.player.onUpdateAbilities();
        }
    }
}