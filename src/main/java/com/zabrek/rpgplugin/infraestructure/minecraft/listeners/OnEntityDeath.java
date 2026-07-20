package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.application.usecases.AddSkillExperienceUseCase;
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
    private final AddSkillExperienceUseCase addSkillExperienceUseCase;
    private final RPGPlugin plugin;

    public OnEntityDeath(PlayerRepository dataManager, RPGPlugin plugin) {
        this.addSkillExperienceUseCase = new AddSkillExperienceUseCase(dataManager);
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player player) {
            SkillProgress updatedProgress = addSkillExperienceUseCase.execute(
                    player.getUniqueId(),
                    event.getDroppedExp()
            );

            if (updatedProgress != null) VisualController.sendLevelBar(plugin, player, updatedProgress);
        }
    }
}
