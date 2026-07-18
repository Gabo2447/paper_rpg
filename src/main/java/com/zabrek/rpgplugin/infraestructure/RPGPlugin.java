package com.zabrek.rpgplugin.infraestructure;

import com.zabrek.rpgplugin.infraestructure.minecraft.commands.SkillsCommand;
import com.zabrek.rpgplugin.infraestructure.minecraft.commands.UseCommand;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.ManaController;
import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.infraestructure.database.SQLiteDataManager;
import com.zabrek.rpgplugin.infraestructure.minecraft.listeners.OnEntityDamage;
import com.zabrek.rpgplugin.infraestructure.minecraft.listeners.OnMenuClick;
import com.zabrek.rpgplugin.infraestructure.minecraft.listeners.OnPlayerJoin;
import com.zabrek.rpgplugin.infraestructure.minecraft.listeners.OnPlayerQuit;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGPlugin extends JavaPlugin {
    private final PlayerRepository dataManager = new SQLiteDataManager(this);
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
        pm.registerEvents(new OnMenuClick(dataManager, this), this);
        pm.registerEvents(new OnPlayerQuit(dataManager), this);

        // Commands
        PluginCommand SkillsCmd = this.getCommand("skills");
        if (SkillsCmd != null) {
            SkillsCmd.setExecutor(new SkillsCommand(this));
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
