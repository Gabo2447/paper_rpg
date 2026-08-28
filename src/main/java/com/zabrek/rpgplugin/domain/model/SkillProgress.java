package com.zabrek.rpgplugin.domain.model;

public class SkillProgress {
    private int level;
    private double experience;

    public SkillProgress(int level, double experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() { return level; }
    public double getExperience() { return experience; }

    public double getRequiredXP() {
        int a = 1000;
        int b = 500;
        int c = 1000;
        return (a * (level * level)) + (b * level) + c; // ax^2+bx+c
    }

    public void addExperience(double amount) {
        this.experience += amount;
        double nextLevelReq = getRequiredXP();

        while (this.experience >= nextLevelReq) {
            this.experience -= nextLevelReq;
            this.level++;
        }
    }
}
