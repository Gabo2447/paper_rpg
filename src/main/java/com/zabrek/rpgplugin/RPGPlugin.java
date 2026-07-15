package com.zabrek.rpgplugin;

import com.zabrek.rpgplugin.commands.JumpCommand;
import com.zabrek.rpgplugin.commands.SkillsCommand;
import com.zabrek.rpgplugin.database.DataManager;

import com.zabrek.rpgplugin.listeners.OnEntityDamage;
import com.zabrek.rpgplugin.listeners.OnMenuClick;
import com.zabrek.rpgplugin.listeners.OnPlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGPlugin extends JavaPlugin {
    private final DataManager dataManager = new DataManager(this);

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnPlayerJoin(dataManager), this);
        pm.registerEvents(new OnEntityDamage(dataManager), this);
        pm.registerEvents(new OnMenuClick(dataManager), this);

        // Commands
        if (this.getCommand("skills") != null) {
            this.getCommand("skills").setExecutor(new SkillsCommand());
        }

        if (this.getCommand("jump") != null) {
            this.getCommand("jump").setExecutor(new JumpCommand(dataManager));
        }
    }

    @Override
    public void onDisable() {
        dataManager.saveAll();
    }
}
