package com.zabrek.rpgplugin.infraestructure.minecraft.controllers;

import com.zabrek.rpgplugin.domain.model.Mana;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

public class VisualController {
    public static void sendManaBar(Player player, Mana mana) {
        Component bar = Component.text("Mana: ", NamedTextColor.AQUA)
                .append(Component.text(mana.getMana() + "/" + mana.getMaxMana(), NamedTextColor.DARK_AQUA));

        player.sendActionBar(bar);
    }

    public static void sendLevelBar(RPGPlugin plugin, Player player, SkillProgress skillProgress) {
        double req = 100 * Math.pow(skillProgress.getLevel(), 1.5); // 100 * (level ** 1.5)
        float progressFraction = (float) Math.clamp(skillProgress.getExperience() / req, 0.0, 1.0);

        Component title = Component.text("Progress Skill - Level ", NamedTextColor.GREEN)
                .append(Component.text(skillProgress.getLevel(), NamedTextColor.DARK_GREEN));

        BossBar bossBar = BossBar.bossBar(title, progressFraction, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        player.showBossBar(bossBar);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.hideBossBar(bossBar);
            }
        }, 60L);
    }
}
