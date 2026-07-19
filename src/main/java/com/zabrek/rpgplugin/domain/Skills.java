package com.zabrek.rpgplugin.domain;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public enum Skills {
    SEISMIC_IMPACT("Seismic Impact", "seismic_impact"),
    STREAK_OF_GOOD_LUCK("Streak of Good Luck", "streak_of_good_luck"),
    PIERCING_ARROW("Piercing Arrow", "piercing_arrow"),
    ADRENALINE_IN_THE_BLOOD("Adrenaline in the Blood", "adrenaline_in_the_blood"),
    OBSIDIAN_SKIN("Obsidian Skin", "obsidian_skin");

    private final String displayName;
    private final String id;

    Skills(String displayName, String id) {
        this.displayName = displayName;
        this.id = id;
    };

    public void execute(Player player) {
        player.sendMessage(Component.text(this.displayName + " selected", NamedTextColor.GREEN));
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }

    public static Skills fromId(String id) {
        for (Skills skill : values()) {
            if (skill.id.equalsIgnoreCase(id)) return skill;
        }
        return null;
    }
}