package com.zabrek.rpgplugin.domain.model;

public class SkillProperties {
    private final int manaCost;
    private final long cooldownTime;

    public SkillProperties(int manaCost, long cooldownTime) {
        this.manaCost = manaCost;
        this.cooldownTime = cooldownTime;
    }

    public int getManaCost() { return manaCost; }
    public long getCooldownTime() { return cooldownTime; }
}
