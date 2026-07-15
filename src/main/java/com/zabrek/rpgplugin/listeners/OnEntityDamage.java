package com.zabrek.rpgplugin.listeners;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.database.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class OnEntityDamage implements Listener {
    private final DataManager dataManager;

    public OnEntityDamage(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
            if (playerData == null) return;

            Skills playerSkill = playerData.getEquippedSkill();
            if (playerSkill == null) return;

            switch (event.getCause()) {
                case FIRE, LAVA, FIRE_TICK -> {
                    if (!(playerSkill == Skills.LAVA_LEATHER)) return;

                    if (ThreadLocalRandom.current().nextInt(4) == 0) { // 25%
                        event.setCancelled(true);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 1));
                    }
                }

                case FALL -> {
                    if (!(playerSkill == Skills.WIND_LEAP)) return;

                    event.setCancelled(true);
                    player.sendMessage(Component.text("Your wind ability saved you from falling!", NamedTextColor.AQUA));
                }
            }
        }
    }
}
