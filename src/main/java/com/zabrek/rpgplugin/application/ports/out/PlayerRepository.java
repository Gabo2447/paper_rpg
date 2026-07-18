package com.zabrek.rpgplugin.application.ports.out;

import java.util.UUID;
import com.zabrek.rpgplugin.domain.model.PlayerData;

public interface PlayerRepository {
    void setup();
    PlayerData getPlayerData(UUID id);
    void loadPlayer(UUID id);
    void savePlayer(UUID id);
    void saveAll(boolean clearCache);
    void unloadPlayer(UUID id);

    void shutdown();
}
