package com.zabrek.rpgplugin.domain;

public enum Skills {
    LAVA_LEATHER("Lava Leather", "LAVA_LEATHER"),
    WIND_LEAP("Wind Leap", "WIND_LEAP");

    private final String displayName;
    private final String id;

    Skills(String displayName, String id) {
        this.displayName = displayName;
        this.id = id;
    };

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }

    public static Skills fromId(String id) {
        for (Skills skill : values()) {
            if (skill.id.equalsIgnoreCase(id)) return skill;
        }
        return null;
    }
}