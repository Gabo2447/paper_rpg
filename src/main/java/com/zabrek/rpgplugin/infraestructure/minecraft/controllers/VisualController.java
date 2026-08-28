package com.zabrek.rpgplugin.infraestructure.minecraft.controllers;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.Mana;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.bossbar.BossBar;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisualController {
    private static final Map<UUID, BossBar> activeBar = new HashMap<>();
    private static final Map<UUID, BukkitTask> hideTasks = new HashMap<>();

    public static void sendManaBar(Player player, Mana mana) {
        Component bar = Component.text("Mana: ", NamedTextColor.AQUA)
                .append(Component.text(mana.getMana() + "/" + mana.getMaxMana(), NamedTextColor.DARK_AQUA));

        player.sendActionBar(bar);
    }

    public static void sendLevelBar(RPGPlugin plugin, Player player, PlayerData data, double expGained) {
        UUID uuid = player.getUniqueId();
        Skills skill = data.getEquippedSkill();

        SkillProgress progress = data.getSkillProgress(skill);

        float percentage = (float) (progress.getExperience() / progress.getRequiredXP());
        percentage = Math.clamp(percentage, 0.0f, 1.0f);

        int percentageInt = (int) (percentage * 100);

        String title = "<gold><bold>+" + (int) expGained + "</bold></gold> " + skill.getDisplayName() + " XP "
                + "<gray>(" + percentageInt + "%)</gray>";

        BossBar bossBar = activeBar.get(uuid);

        if (bossBar == null) {
            bossBar = BossBar.bossBar(
                    MiniMessage.miniMessage().deserialize(title),
                    percentage,
                    BossBar.Color.RED,
                    BossBar.Overlay.PROGRESS
            );
            activeBar.put(uuid, bossBar);
            player.showBossBar(bossBar);
        } else {
            bossBar.name(MiniMessage.miniMessage().deserialize(title));
            bossBar.progress(percentage);
        }

        if (hideTasks.containsKey(uuid)) hideTasks.get(uuid).cancel();

        final BossBar finalBar = bossBar;
        BukkitTask newTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.hideBossBar(finalBar);
                activeBar.remove(uuid);
                hideTasks.remove(uuid);
            }
        }.runTaskLater(plugin, 60L);

        hideTasks.put(uuid, newTask);
    }

    public static void removeBarOnQuit(Player player) {
        UUID uuid = player.getUniqueId();
        if (activeBar.containsKey(uuid)) {
            player.hideBossBar(activeBar.get(uuid));
            activeBar.remove(uuid);
        }
        if (hideTasks.containsKey(uuid)) {
            hideTasks.get(uuid).cancel();
            hideTasks.remove(uuid);
        }
    }
}
