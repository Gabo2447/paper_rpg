package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class OnPlayerConfiguration implements Listener {
    private final PlayerRepository dataManager;

    public OnPlayerConfiguration(PlayerRepository dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerConfiguration(AsyncPlayerConnectionConfigureEvent event) {
        PlayerConfigurationConnection conn = event.getConnection();
        var profile = conn.getProfile();

        if (profile.getId() == null) {
            conn.disconnect(Component.text("We were unable to verify your profile. Please try again later.", NamedTextColor.RED));
        }

        UUID playerUniqueId = conn.getProfile().getId();
        dataManager.loadPlayer(playerUniqueId);
    }
}
