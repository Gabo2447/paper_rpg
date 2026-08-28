package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.application.usecases.AddSkillExperienceUseCase;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.domain.shared.DomainRules;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.VisualController;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnEntityDeath implements Listener {
    private final AddSkillExperienceUseCase addSkillExperienceUseCase;
    private final RPGPlugin plugin;
    private final NamespacedKey key;
    private final PlayerRepository dataManager;

    public OnEntityDeath(PlayerRepository dataManager, RPGPlugin plugin) {
        this.dataManager = dataManager;
        this.addSkillExperienceUseCase = new AddSkillExperienceUseCase(dataManager);
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, DomainRules.NAMESPACED_KEY);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player player) {
            LivingEntity mob = event.getEntity();
            if (mob instanceof Player) return;

            Integer mobLevel = mob.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            if (mobLevel == null) return;

            int xp = event.getDroppedExp();
            int finalXP = addSkillExperienceUseCase.calculateXP(xp, mobLevel);

            PlayerData data = dataManager.getPlayerData(player.getUniqueId());
            if (data == null) return;

            SkillProgress updatedProgress = addSkillExperienceUseCase.execute(
                    player.getUniqueId(),
                    xp,
                    mobLevel
            );

            if (updatedProgress != null) VisualController.sendLevelBar(plugin, player, data, finalXP);
        }
    }
}
