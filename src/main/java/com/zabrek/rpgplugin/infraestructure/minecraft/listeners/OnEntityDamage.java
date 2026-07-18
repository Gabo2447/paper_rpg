package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillBehavior;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillRegistry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamage implements Listener {
    private final PlayerRepository dataManager;
    private final SkillRegistry skillRegistry;

    public OnEntityDamage(PlayerRepository dataManager, SkillRegistry skillRegistry) {
        this.dataManager = dataManager;
        this.skillRegistry = skillRegistry;
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
            if (playerData == null) return;

            Skills playerSkill = playerData.getEquippedSkill();
            if (playerSkill == null) return;

            SkillBehavior behavior = skillRegistry.getBehavior(playerSkill);
            if (behavior == null) return;

            behavior.onDamage(event, player);
        }
    }
}
