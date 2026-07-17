package com.zabrek.rpgplugin.database;

import com.zabrek.rpgplugin.Skills;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private Skills equippedSkill;
    private final Map<String, Long> cooldowns;
    private final Mana mana;

    public PlayerData() {
        this.mana = new Mana();
        this.equippedSkill = null;
        this.cooldowns = new HashMap<>();
    }

    public Mana getMana() { return mana; }

    public Skills getEquippedSkill() {
        return equippedSkill;
    }
    public void setEquippedSkill(Skills equippedSkill) {
        this.equippedSkill = equippedSkill;
    }

    public Map<String, Long> getCooldowns() { return cooldowns; }

    public boolean isOnCooldown(String skill) {
        if (!cooldowns.containsKey(skill)) return false;
        return System.currentTimeMillis() < cooldowns.get(skill);
    }

    public void addCooldown(String skill, long seg) {
        long timeExpiration = System.currentTimeMillis() + (seg * 1000);
        cooldowns.put(skill, timeExpiration);
    }
}
