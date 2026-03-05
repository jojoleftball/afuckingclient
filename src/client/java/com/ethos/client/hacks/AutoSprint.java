package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public class AutoSprint extends Hack {
    public AutoSprint() {
        super("AutoSprint", HackCategory.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null) return;

        // Check if W key is pressed (forward movement)
        if (client.options.keyUp.isDown()) {
            // Check if we can sprint (not sneaking, not hungry, not colliding)
            if (!client.player.isCrouching() &&
                client.player.getFoodData().getFoodLevel() > 6 &&
                !client.player.horizontalCollision &&
                !client.player.isSprinting()) { // Only set if not already sprinting

                client.player.setSprinting(true);
            }
        }
    }
}