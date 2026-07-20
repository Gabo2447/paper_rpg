package com.zabrek.rpgplugin.infraestructure;

import com.zabrek.rpgplugin.infraestructure.minecraft.commands.SkillsCommand;
import com.zabrek.rpgplugin.infraestructure.minecraft.commands.UseCommand;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.ManaController;
import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.infraestructure.database.SQLiteDataManager;
import com.zabrek.rpgplugin.infraestructure.minecraft.listeners.*;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillRegistry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RPGPlugin extends JavaPlugin {
    private final PlayerRepository dataManager = new SQLiteDataManager(this);
    private final SkillRegistry skillRegistry = new SkillRegistry();

    private ManaController manaController;

    @Override
    public void onEnable() {
        dataManager.setup();

        this.manaController = new ManaController(this, dataManager);
        Bukkit.getScheduler().runTaskTimer(this, this.manaController, 0L, 40L);

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        manaController.stop();
        dataManager.saveAll(true);
        dataManager.shutdown();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        List.of(
                new OnPlayerJoin(),
                new OnMenuClick(dataManager),
                new OnPlayerConfiguration(dataManager),
                new OnPlayerQuit(dataManager),
                new OnEntityDamage(dataManager, skillRegistry),
                new OnEntityDeath(dataManager, this)
        ).forEach(listener -> pm.registerEvents(listener, this));
    }

    private void registerCommands() {
        registerCommand("skills", new SkillsCommand(this));
        registerCommand("use", new UseCommand(dataManager, skillRegistry));
    }

    private void registerCommand(String name, CommandExecutor executor) {
        var command = this.getCommand(name);
        if (command == null) {
            this.getLogger().severe("Critic Error: The command " + name + " is not register on plugin.yml");
            return;
        }
        command.setExecutor(executor);
    }
}
