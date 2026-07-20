package com.zabrek.rpgplugin.domain.model;

import com.zabrek.rpgplugin.domain.Skills;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {
    private Skills equippedSkill;
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<String, SkillProgress> skillMatrix = new ConcurrentHashMap<>();
    private final Mana mana;

    public PlayerData() {
        this.mana = new Mana();
        this.equippedSkill = null;
    }

    public boolean hasSkillEquipped() { return equippedSkill != null; }

    public boolean isSkillOnCooldown(Skills skill) {
        Long expiration = cooldowns.get(skill.getId());
        if (expiration == null) return false;
        return System.currentTimeMillis() < expiration;
    }

    public void applyCooldown(Skills skill, long seconds) {
        long expirationTime = System.currentTimeMillis() + (seconds * 1000);
        cooldowns.put(skill.getId(), expirationTime);
    }

    public long getRemainingCooldownsMillis(Skills skill) {
        Long expiration = cooldowns.get(skill.getId());
        if (expiration == null) return 0;
        return Math.max(0, expiration - System.currentTimeMillis());
    }


    public SkillProgress getSkillProgress(Skills skill) { return this.skillMatrix.get(skill.getId()); }

    public void addSkillExperience(Skills skill, double experience) {
        SkillProgress progress = this.skillMatrix.computeIfAbsent(
                skill.getId(),
                k -> new SkillProgress(1, 0.0)
        );
        progress.addExperience(experience);
    }

    // --- Getters & Setts ---
    public Mana getMana() { return mana; }

    public Skills getEquippedSkill() { return equippedSkill; }
    public void setEquippedSkill(Skills equippedSkill) { this.equippedSkill = equippedSkill; }

    public Map<String, Long> getCooldownsView() { return Collections.unmodifiableMap(cooldowns); }
    public Map<String, SkillProgress> getSkillPropertiesView() { return Collections.unmodifiableMap(skillMatrix); }

    public void convertAndLoadLevels(Map<String, SkillProgress> loadedLevels) { this.skillMatrix.putAll(loadedLevels); }
    public void convertAndLoadCooldowns(Map<String, Long> loadedCooldowns) { this.cooldowns.putAll(loadedCooldowns); }
}
