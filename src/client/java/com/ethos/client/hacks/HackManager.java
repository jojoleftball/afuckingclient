package com.ethos.client.hacks;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private static final List<Hack> hacks = new ArrayList<>();
    public static final ScaffoldWalk SCAFFOLD = new ScaffoldWalk();
    public static final Killaura KILLAURA = new Killaura();
    public static final FullBright FULLBRIGHT = new FullBright();
    public static final Jesus JESUS = new Jesus();
    public static final Spider SPIDER = new Spider();
    public static final AutoSprint AUTOSPRINT = new AutoSprint();
    public static final MobESP MOBESP = new MobESP();
    public static final PlayerESP PLAYERESP = new PlayerESP();
    public static final Flight FLIGHT = new Flight();
    public static final NoFall NOFALL = new NoFall();

    public static void init() {
        // Set default keybinds for hacks
        KILLAURA.setKeybind(GLFW.GLFW_KEY_R);
        FULLBRIGHT.setKeybind(GLFW.GLFW_KEY_B);
        JESUS.setKeybind(GLFW.GLFW_KEY_J);
        SPIDER.setKeybind(GLFW.GLFW_KEY_V);
        AUTOSPRINT.setKeybind(GLFW.GLFW_KEY_Z);
        MOBESP.setKeybind(GLFW.GLFW_KEY_X);
        PLAYERESP.setKeybind(GLFW.GLFW_KEY_C);
        FLIGHT.setKeybind(GLFW.GLFW_KEY_F);
        NOFALL.setKeybind(GLFW.GLFW_KEY_N);
        SCAFFOLD.setKeybind(GLFW.GLFW_KEY_G);

        hacks.add(SCAFFOLD);
        hacks.add(KILLAURA);
        hacks.add(FULLBRIGHT);
        hacks.add(JESUS);
        hacks.add(SPIDER);
        hacks.add(AUTOSPRINT);
        hacks.add(MOBESP);
        hacks.add(PLAYERESP);
        hacks.add(FLIGHT);
        hacks.add(NOFALL);
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