package com.ethos.client.hacks;

import net.minecraft.client.Minecraft;

public abstract class Hack {
    private final String name;
    private final HackCategory category;
    private boolean enabled;

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

    /**
     * Called every client tick when the hack system is active.
     */
    public abstract void onTick(Minecraft client);
}