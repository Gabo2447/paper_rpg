package com.zabrek.rpgplugin;

public enum Skills {
    LAVA_LEATHER("Lava Leather"),
    WIND_LEAP("Wind Leap");

    private final String displayName;

    Skills(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return this.displayName; }
}
