package com.ethos.client.hacks;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private static final List<Hack> hacks = new ArrayList<>();
    public static final ScaffoldWalk SCAFFOLD = new ScaffoldWalk();

    public static void init() {
        hacks.add(SCAFFOLD);
        ClientTickEvents.END_CLIENT_TICK.register(HackManager::onTick);
    }

    private static void onTick(Minecraft client) {
        for (Hack hack : hacks) {
            hack.onTick(client);
        }
    }

    public static List<Hack> getHacks() {
        return hacks;
    }

    /**
     * Returns hacks grouped by their category. Categories with no hacks are omitted.
     */
    public static java.util.Map<HackCategory, java.util.List<Hack>> getHacksByCategory() {
        java.util.Map<HackCategory, java.util.List<Hack>> map = new java.util.EnumMap<>(HackCategory.class);
        for (Hack hack : hacks) {
            map.computeIfAbsent(hack.getCategory(), k -> new java.util.ArrayList<>()).add(hack);
        }
        return map;
    }
}