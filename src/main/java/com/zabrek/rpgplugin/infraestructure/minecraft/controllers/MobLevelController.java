package com.zabrek.rpgplugin.infraestructure.minecraft.controllers;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class MobLevelController {
    public static void setCustomName(LivingEntity mob, int mobLevel) {
        LevelTier tier = LevelTier.fromLevel(mobLevel);

        String mobName = mob.getType().name().substring(0, 1).toUpperCase() + mob.getType().name().substring(1).toLowerCase();
        String message = tier.getColorTag() + "[Lvl. " + mobLevel + "] " + mobName;

        mob.customName(MiniMessage.miniMessage().deserialize(message));
        mob.setCustomNameVisible(true);
    }

    /*
    * Increases health by 15% per level
    * Increases damage by 8% per level
    * Increases speed by 1% per level (max 25%)
    * Increases resistance to thrust by 3% per level (starting at level 10 and reaching a maximum of 50%)
    * */
    public static void setMobLevel(LivingEntity mob, int level) {
        AttributeInstance maxHealth = mob.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealth != null) {
            double newMaxHealth = maxHealth.getBaseValue() * (1.0 + (level * 0.15));
            maxHealth.setBaseValue(newMaxHealth);
            mob.setHealth(newMaxHealth);
        }

        AttributeInstance attackDamage = mob.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackDamage != null) {
            double newAttack = attackDamage.getBaseValue() * (1.0 + (level * 0.08));
            attackDamage.setBaseValue(newAttack);
        }

        AttributeInstance speed = mob.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speed != null) {
            double extraSpeed = Math.min(level * 0.01, 0.25); // Max 25%
            double newSpeed = speed.getBaseValue() * (1.0 + extraSpeed);
            speed.setBaseValue(newSpeed);
        }

        AttributeInstance knockbackRes = mob.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (knockbackRes != null && level >= 10) {
            double resistance = Math.min((level - 9) * 0.03, 0.5);
            knockbackRes.setBaseValue(resistance);
        }
    }

    private enum LevelTier {
        EASY("Easy", "<green>", 1, 5),
        MEDIUM("Medium", "<yellow>", 6, 15),
        HARD("Hard", "<red>", 16, 30),
        EXTREME("Extreme", "<dark_red>", 31, Integer.MAX_VALUE);

        private final String label;
        private final String colorTag;
        private final int minLevel;
        private final int maxLevel;

        LevelTier(String label, String colorTag, int minLevel, int maxLevel) {
            this.label = label;
            this.colorTag = colorTag;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        public static LevelTier fromLevel(int level) {
            for (LevelTier tier : values()) {
                if (level >= tier.minLevel && level <= tier.maxLevel) {
                    return tier;
                }
            }
            return EXTREME;
        }

        public String getLabel() { return label; }
        public String getColorTag() { return colorTag; }
    }
}
