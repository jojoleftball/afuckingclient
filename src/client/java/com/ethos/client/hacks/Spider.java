package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public class Spider extends Hack {
    private final NumberSetting climbSpeed = new NumberSetting("Climb Speed", "How fast to climb walls", 0.2, 0.1, 1.0);

    public Spider() {
        super("Spider", HackCategory.MOVEMENT);
        addSetting(climbSpeed);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null) return;

        // Check if player is colliding horizontally (walking into a wall)
        if (client.player.horizontalCollision && client.player.getDeltaMovement().y > -0.2) {
            // Apply upward force to climb the wall
            client.player.setDeltaMovement(
                client.player.getDeltaMovement().x,
                climbSpeed.getValue(),
                client.player.getDeltaMovement().z
            );
        }
    }
}