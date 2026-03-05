package com.ethos.client.hacks;

public abstract class HackSetting<T> {
    protected final String name;
    protected final String description;
    protected T value;
    protected T minValue;
    protected T maxValue;

    public HackSetting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
    }

    public HackSetting(String name, String description, T defaultValue, T minValue, T maxValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMinValue() {
        return minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public abstract String getValueString();
}