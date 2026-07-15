package com.zabrek.rpgplugin.database;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public interface DataManager {
    void setup();
    PlayerData getPlayerData(UUID id);
    void loadPlayer(UUID id);
    void savePlayer(UUID id);
    void saveAll(boolean clearCache);
    void unloadPlayer(UUID id);

    FileConfiguration getConfig();
}
