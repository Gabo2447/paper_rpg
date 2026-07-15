package com.zabrek.rpgplugin.listeners;

import com.zabrek.rpgplugin.database.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerQuit implements Listener {
    private final DataManager dataManager;

    public OnPlayerQuit(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        dataManager.savePlayer(id);
        dataManager.unloadPlayer(id);
    }
}
