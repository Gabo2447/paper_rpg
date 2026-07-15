package com.zabrek.rpgplugin;

import com.zabrek.rpgplugin.commands.*;
import com.zabrek.rpgplugin.listeners.*;
import com.zabrek.rpgplugin.database.DataManager;
import com.zabrek.rpgplugin.database.types.YMLDataManager;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGPlugin extends JavaPlugin {
    private final DataManager dataManager = new YMLDataManager(this);

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnPlayerJoin(dataManager), this);
        pm.registerEvents(new OnEntityDamage(dataManager), this);
        pm.registerEvents(new OnMenuClick(dataManager), this);
        pm.registerEvents(new OnPlayerQuit(dataManager), this);

        // Commands
        PluginCommand SkillsCmd = this.getCommand("skills");
        if (SkillsCmd != null) {
            SkillsCmd.setExecutor(new SkillsCommand());
        }

        PluginCommand JumpCmd = this.getCommand("jump");
        if (JumpCmd != null) {
            JumpCmd.setExecutor(new JumpCommand(dataManager));
        }
    }

    @Override
    public void onDisable() {
        dataManager.saveAll(true);
    }
}
