package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import com.ethos.client.hacks.HackCategory;

public class ScaffoldWalk extends Hack {
    public ScaffoldWalk() {
        super("ScaffoldWalk", HackCategory.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!isEnabled()) return;
        if (client == null || client.player == null || client.level == null) return;

        // only scaffold while player is moving on ground
        if (client.player.zza == 0 && client.player.xxa == 0) return;
        if (!client.player.onGround()) return;

        BlockPos below = client.player.blockPosition().below();
        if (client.level.isEmptyBlock(below)) {
            // place a simple stone block under the player (client-side only)
            client.level.setBlock(below, Blocks.STONE.defaultBlockState(), 3);
        }
    }

}
