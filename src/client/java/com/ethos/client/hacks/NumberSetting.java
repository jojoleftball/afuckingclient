package com.ethos.client.hacks;

public class NumberSetting extends HackSetting<Double> {
    public NumberSetting(String name, String description, double defaultValue, double minValue, double maxValue) {
        super(name, description, defaultValue, minValue, maxValue);
    }

    @Override
    public String getValueString() {
        return String.format("%.2f", value);
    }
}