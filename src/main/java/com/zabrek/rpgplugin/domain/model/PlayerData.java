package com.zabrek.rpgplugin.domain.model;

import com.zabrek.rpgplugin.domain.Skills;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {
    private Skills equippedSkill;
    private final Map<String, Long> cooldowns;
    private final Map<String, SkillProgress> skillMatrix = new ConcurrentHashMap<>();
    private final Mana mana;

    public PlayerData() {
        this.mana = new Mana();
        this.equippedSkill = null;
        this.cooldowns = new ConcurrentHashMap<>();
    }

    public Mana getMana() { return mana; }
    public Skills getEquippedSkill() {
        return equippedSkill;
    }
    public Map<String, Long> getCooldowns() { return cooldowns; }
    public Map<String, SkillProgress> getSkillProperties() { return skillMatrix; }

    public void setEquippedSkill(Skills equippedSkill) {
        this.equippedSkill = equippedSkill;
    }

    public boolean isOnCooldown(String skill) {
        Long expiration = cooldowns.get(skill);
        if (expiration == null) return false;

        if (System.currentTimeMillis() < expiration) return true;

        cooldowns.remove(skill);
        return false;
    }

    public void addCooldown(String skill, long seg) {
        long timeExpiration = System.currentTimeMillis() + (seg * 1000);
        cooldowns.put(skill, timeExpiration);
    }
}
