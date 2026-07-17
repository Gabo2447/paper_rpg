package com.zabrek.rpgplugin.controllers;

import com.zabrek.rpgplugin.database.DataManager;
import com.zabrek.rpgplugin.database.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.function.Consumer;

public class ManaController implements Consumer<BukkitTask> {
    private final JavaPlugin plugin;
    private final DataManager data;

    private volatile boolean running = true;
    private int ADD_MANA_PER_CYCLE = 10;

    public ManaController(JavaPlugin plugin, DataManager dataManager) {
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
            playerData.getMana().addMana(ADD_MANA_PER_CYCLE);
            VisualController.sendManaBar(player, playerData.getMana());
        }
    }

    public void stop() {
        this.running = false;
    }
}
