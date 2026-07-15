package com.zabrek.rpgplugin.listeners;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.database.*;

import com.zabrek.rpgplugin.skills.SkillBehavior;
import com.zabrek.rpgplugin.skills.SkillRegistry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamage implements Listener {
    private final DataManager dataManager;
    private final SkillRegistry skillRegistry;

    public OnEntityDamage(DataManager dataManager, SkillRegistry skillRegistry) {
        this.dataManager = dataManager;
        this.skillRegistry = skillRegistry;
    }

    @EventHandler
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
