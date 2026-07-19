package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.VisualController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;
import java.util.UUID;

public class OnEntityDeath implements Listener {
    private final PlayerRepository dataManager;
    private final RPGPlugin plugin;

    public OnEntityDeath(PlayerRepository dataManager, RPGPlugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player player) {
            UUID id = player.getUniqueId();
            PlayerData data = dataManager.getPlayerData(id);
            if (data == null) return;

            Skills equippedSkill = data.getEquippedSkill();
            if (equippedSkill == null) return;

            Map<String, SkillProgress> skillProgressMap = data.getSkillProperties();

            SkillProgress skillProgress = skillProgressMap.computeIfAbsent(
                    equippedSkill.getId(),
                    k -> new SkillProgress(1, 0.0)
            );

            int xpDropped = event.getDroppedExp();
            if (xpDropped > 0) {
                skillProgress.addExperience(xpDropped * 1.5);
                VisualController.sendLevelBar(plugin, player, skillProgress);
            }
        }
    }
}
