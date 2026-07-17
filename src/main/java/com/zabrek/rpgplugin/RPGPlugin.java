package com.zabrek.rpgplugin;

import com.zabrek.rpgplugin.commands.*;
import com.zabrek.rpgplugin.controllers.ManaController;
import com.zabrek.rpgplugin.database.types.SQLiteDataManager;
import com.zabrek.rpgplugin.listeners.*;
import com.zabrek.rpgplugin.database.DataManager;
import com.zabrek.rpgplugin.database.types.YMLDataManager;

import com.zabrek.rpgplugin.skills.SkillRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGPlugin extends JavaPlugin {
    private final DataManager dataManager = new SQLiteDataManager(this);
    private final SkillRegistry skillRegistry = new SkillRegistry();

    private ManaController manaController;

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        dataManager.setup();

        this.manaController = new ManaController(this, dataManager);
        Bukkit.getScheduler().runTaskTimer(this, this.manaController, 0L, 40L);

        pm.registerEvents(new OnPlayerJoin(dataManager), this);
        pm.registerEvents(new OnEntityDamage(dataManager, skillRegistry), this);
        pm.registerEvents(new OnMenuClick(dataManager), this);
        pm.registerEvents(new OnPlayerQuit(dataManager), this);

        // Commands
        PluginCommand SkillsCmd = this.getCommand("skills");
        if (SkillsCmd != null) {
            SkillsCmd.setExecutor(new SkillsCommand());
        }

        PluginCommand UseCmd = this.getCommand("use");
        if (UseCmd != null) {
            UseCmd.setExecutor(new UseCommand(dataManager, skillRegistry));
        }
    }

    @Override
    public void onDisable() {
        manaController.stop();
        dataManager.saveAll(true);
        dataManager.shutdown();
    }
}
