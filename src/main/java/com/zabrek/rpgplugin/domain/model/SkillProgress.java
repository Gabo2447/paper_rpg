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

    public void addExperience(double amount) {
        this.experience += amount;
        double nextLevelReq = 100 * Math.pow(level, 1.5);

        while (this.experience >= nextLevelReq) {
            this.experience -= nextLevelReq;
            this.level++;
        }
    }
}
