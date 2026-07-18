package com.zabrek.rpgplugin.domain;

public enum Skills {
    LAVA_LEATHER("Lava Leather", 15, 10),
    WIND_LEAP("Wind Leap", 20, 10);

    private final String displayName;
    private final int manaCost;
    private final long cooldownTime;

    Skills(String displayName, int manaCost, long cooldownTime) {
        this.displayName = displayName;
        this.manaCost = manaCost;
        this.cooldownTime = cooldownTime;
    };

    public String getDisplayName() { return displayName; }
    public int getManaCost() { return manaCost; }
    public long getCooldownTime() { return cooldownTime; }
}