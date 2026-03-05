package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;

public class Killaura extends Hack {
    private final NumberSetting range = new NumberSetting("Range", "Maximum attack distance", 4.5, 1.0, 10.0);
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", "Only attack players, not mobs", false);

    public Killaura() {
        super("Killaura", HackCategory.COMBAT);
        addSetting(range);
        addSetting(playersOnly);
    }

    @Override
    public void onTick(Minecraft client) {
        if (!this.isEnabled() || client.player == null || client.level == null) return;

        // Only attack when attack strength is at 100%
        if (client.player.getAttackStrengthScale(0.0F) < 1.0F) return;

        // Find closest valid target
        LivingEntity target = findTarget(client);
        if (target != null) {
            // Look at the target's hitbox
            lookAtTarget(client, target);

            // Attack the target
            client.gameMode.attack(client.player, target);
            client.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private LivingEntity findTarget(Minecraft client) {
        LivingEntity closestTarget = null;
        double closestDistance = range.getValue() * range.getValue();

        // Check all entities in the world
        for (var entity : client.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (livingEntity == client.player) continue; // Don't attack yourself
            if (!livingEntity.isAlive()) continue;

            // Check target type filter
            if (playersOnly.getValue()) {
                if (!(livingEntity instanceof Player)) continue;
            } else {
                // Attack both players and hostile mobs
                if (!(livingEntity instanceof Player) && !(livingEntity instanceof Mob)) continue;
            }

            // Check distance
            double distance = client.player.distanceToSqr(livingEntity);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestTarget = livingEntity;
            }
        }

        return closestTarget;
    }

    private void lookAtTarget(Minecraft client, LivingEntity target) {
        // Calculate the position to look at (center of target's hitbox)
        Vec3 targetPos = target.getEyePosition();
        Vec3 playerPos = client.player.getEyePosition();

        // Calculate the direction vector
        Vec3 direction = targetPos.subtract(playerPos).normalize();

        // Convert to yaw and pitch
        double yaw = Math.toDegrees(Math.atan2(-direction.x, direction.z));
        double pitch = Math.toDegrees(-Math.asin(direction.y));

        // Set the player's rotation
        client.player.setYRot((float) yaw);
        client.player.setXRot((float) pitch);
    }
}