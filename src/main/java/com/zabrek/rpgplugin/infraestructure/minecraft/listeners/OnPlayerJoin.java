package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    public OnPlayerJoin() {}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(Component.text("Hello, " + player.getName() + "!"));
        player.sendMessage(Component.text("TIP: Try using the “/skills” command to select your class", NamedTextColor.GOLD));
        player.sendMessage(Component.text("If you're going to select your class, you can use the “/use” command."));
    }

}
