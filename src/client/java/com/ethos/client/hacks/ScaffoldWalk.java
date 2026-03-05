package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class ScaffoldWalk extends Hack {
    public ScaffoldWalk() {
        super("ScaffoldWalk", HackCategory.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null || client.level == null) return;

        // Get the position exactly under the player's feet
        BlockPos posBelow = client.player.blockPosition().below();

        // Only place if it's currently air
        if (client.level.getBlockState(posBelow).isAir()) {

            // Find a block in the hotbar (slots 0-8)
            int blockSlot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = client.player.getInventory().getItem(i);
                if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                    blockSlot = i;
                    break;
                }
            }

            if (blockSlot != -1) {
                // Switch to block slot
                int oldSlot = client.player.getInventory().getSelectedSlot();
                client.player.getInventory().setSelectedSlot(blockSlot);

                // Place the block by simulating right-click on the position below
                placeBlock(client, posBelow);

                // Switch back to original slot
                client.player.getInventory().setSelectedSlot(oldSlot);
            }
        }
    }

    private void placeBlock(Minecraft client, BlockPos pos) {
        // Find a solid block adjacent to the position we want to place at
        // We need to place against an existing block, not in the air

        // Check all directions around the target position for a solid block
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            if (!client.level.getBlockState(adjacentPos).isAir()) {
                // Found a solid block, place against it
                Vec3 hitVec = new Vec3(
                    adjacentPos.getX() + 0.5 + direction.getStepX() * 0.5,
                    adjacentPos.getY() + 0.5 + direction.getStepY() * 0.5,
                    adjacentPos.getZ() + 0.5 + direction.getStepZ() * 0.5
                );

                BlockHitResult hitResult = new BlockHitResult(hitVec, direction.getOpposite(), adjacentPos, false);

                // Interact with the block (place it)
                client.gameMode.useItemOn(client.player, InteractionHand.MAIN_HAND, hitResult);

                // Swing hand for visual feedback
                client.player.swing(InteractionHand.MAIN_HAND);
                return; // Successfully placed, exit
            }
        }
    }
}