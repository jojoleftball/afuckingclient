package com.ethos.client.hacks;

public class BooleanSetting extends HackSetting<Boolean> {
    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public String getValueString() {
        return value ? "Enabled" : "Disabled";
    }
}