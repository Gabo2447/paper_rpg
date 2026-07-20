package com.zabrek.rpgplugin.infraestructure.minecraft.controllers;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.function.Consumer;

public class ManaController implements Consumer<BukkitTask> {
    private final RPGPlugin plugin;
    private final PlayerRepository data;

    private volatile boolean running = true;
    private final int ADD_MANA_PER_CYCLE = 10;

    public ManaController(RPGPlugin plugin, PlayerRepository dataManager) {
        this.plugin = plugin;
        this.data = dataManager;
    }

    @Override
    public void accept(BukkitTask task) {
        if (!running) {
            task.cancel();
            return;
        }
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        for (Player player : players) {
            PlayerData playerData = data.getPlayerData(player.getUniqueId());
            if (playerData == null) continue;

            playerData.getMana().addMana(ADD_MANA_PER_CYCLE);
            VisualController.sendManaBar(player, playerData.getMana());
        }
    }

    public void stop() {
        this.running = false;
    }
}
