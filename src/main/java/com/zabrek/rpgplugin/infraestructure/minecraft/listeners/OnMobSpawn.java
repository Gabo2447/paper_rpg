package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.usecases.GetLevelMobUseCase;
import com.zabrek.rpgplugin.domain.shared.DomainRules;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.MobLevelController;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class OnMobSpawn implements Listener {
    private final GetLevelMobUseCase useCase;
    private final NamespacedKey key;

    public OnMobSpawn(RPGPlugin plugin) {
        this.key = new NamespacedKey(plugin, DomainRules.NAMESPACED_KEY);
        this.useCase = new GetLevelMobUseCase();
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        LivingEntity mob = event.getEntity();
        if (mob instanceof Player) return;

        int level = useCase.execute(mob.getX(), mob.getZ());
        mob.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);

        MobLevelController.setMobLevel(mob, level);
        MobLevelController.setCustomName(mob, level);
    }
}
