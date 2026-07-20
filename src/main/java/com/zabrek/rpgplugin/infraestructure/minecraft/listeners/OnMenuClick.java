package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.infraestructure.minecraft.commands.inventory.RPGMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnMenuClick implements Listener {
    private final PlayerRepository dataManager;

    public OnMenuClick(PlayerRepository dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof RPGMenu menu) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;
            menu.handleMenuClick(event, dataManager);
        }
    }
}
