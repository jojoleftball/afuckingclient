package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.BlockState;

public class Jesus extends Hack {
    public Jesus() {
        super("Jesus", HackCategory.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null || client.level == null) return;

        // Check if player is in water or lava
        BlockPos playerPos = client.player.blockPosition();
        BlockState blockState = client.level.getBlockState(playerPos);

        boolean inLiquid = blockState.getFluidState().isSource() ||
                          blockState.getFluidState().getType() == Fluids.WATER ||
                          blockState.getFluidState().getType() == Fluids.LAVA;

        if (inLiquid && !client.player.isCrouching()) {
            // Set upward velocity to stay on surface
            client.player.setDeltaMovement(
                client.player.getDeltaMovement().x,
                0.0001, // Tiny upward force to stay on surface
                client.player.getDeltaMovement().z
            );

            // Prevent sinking animation
            if (client.player.getDeltaMovement().y < 0) {
                client.player.setDeltaMovement(
                    client.player.getDeltaMovement().x,
                    0,
                    client.player.getDeltaMovement().z
                );
            }
        }
    }
}