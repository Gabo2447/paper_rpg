package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerQuit implements Listener {
    private final PlayerRepository dataManager;

    public OnPlayerQuit(PlayerRepository dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(Component.text("[+] ", NamedTextColor.RED)
                .append(Component.text("The player ", NamedTextColor.GRAY))
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .append(Component.text(" has disconnected.", NamedTextColor.GRAY)));

        UUID id = event.getPlayer().getUniqueId();
        dataManager.savePlayer(id);
        dataManager.unloadPlayer(id);
    }
}
