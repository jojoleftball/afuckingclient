package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class Hack {
    private final String name;
    private final HackCategory category;
    private boolean enabled;
    private int keybind = -1; // -1 means no keybind
    private final List<HackSetting<?>> settings = new ArrayList<>();

    protected Hack(String name, HackCategory category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public HackCategory getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public List<HackSetting<?>> getSettings() {
        return settings;
    }

    protected void addSetting(HackSetting<?> setting) {
        settings.add(setting);
    }

    /**
     * Called every client tick when the hack system is active.
     */
    public abstract void onTick(Minecraft client);
}