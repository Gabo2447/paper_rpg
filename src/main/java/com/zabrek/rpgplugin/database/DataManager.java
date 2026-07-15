package com.zabrek.rpgplugin.database;

import com.zabrek.rpgplugin.RPGPlugin;
import com.zabrek.rpgplugin.Skills;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final RPGPlugin plugin;
    private File file;
    private FileConfiguration config;

    private final Map<UUID, PlayerData> memoryRam;

    public DataManager(RPGPlugin plugin) {
        this.plugin = plugin;
        this.memoryRam = new HashMap<>();
        setup();
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "players.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("The players.yml file could not be created!");

            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public PlayerData getPlayerData(UUID id) {
        return memoryRam.get(id);
    }

    public void loadPlayer(UUID id) {
        PlayerData data = new PlayerData();
        String path = "players." + id.toString();

        if (config.contains(path)) {
            if (config.isString(path + ".skill_equipped")) {
                String savedSkill = config.getString(path + ".skill_equipped");
                if (savedSkill != null && !savedSkill.equals("none")) {
                    try {
                        data.setEquippedSkill(Skills.valueOf(savedSkill));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Habilidad desconocida para " + id + ": " + savedSkill);
                        data.setEquippedSkill(null);
                    }
                }
            }

            // Cargar los Cooldowns
            if (config.getConfigurationSection(path + ".cooldowns") != null) {
                for (String skillKey : config.getConfigurationSection(path + ".cooldowns").getKeys(false)) {
                    long timeExpiration = config.getLong(path + ".cooldowns." + skillKey);
                    data.getCooldowns().put(skillKey, timeExpiration);
                }
            }
        }

        memoryRam.put(id, data);
    }

    public void savePlayer(UUID id) {
        PlayerData data = memoryRam.get(id);
        if (data == null) return;

        String path = "players." + id.toString();

        Skills skill = data.getEquippedSkill();
        config.set(path + ".skill_equipped", (skill != null) ? skill.name(): "none");

        config.set(path + ".cooldowns", null);

        for (Map.Entry<String , Long> entry : data.getCooldowns().entrySet()) {
            if (System.currentTimeMillis() < entry.getValue()) {
                config.set(path + ".cooldowns." + entry.getKey(), entry.getValue());
            }
        }

        memoryRam.remove(id);
        saveConfig();
    }

    public void saveAll() {
        for (UUID id : new HashMap<>(memoryRam).keySet()) {
            savePlayer(id);
        }
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("The players.yml file could not be saved!");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
