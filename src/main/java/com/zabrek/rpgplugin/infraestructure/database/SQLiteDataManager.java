package com.zabrek.rpgplugin.infraestructure.database;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import com.zabrek.rpgplugin.domain.Skills;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SQLiteDataManager implements PlayerRepository {
    private final RPGPlugin plugin;
    private final Map<UUID, PlayerData> temporalMemory;
    private final HikariDataSource config;

    private final ScheduledExecutorService dbExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "RPGPlugin-Database-Thread");
        thread.setDaemon(true);
        return thread;
    });

    public SQLiteDataManager(RPGPlugin plugin) {
        this.plugin = plugin;
        this.temporalMemory = new HashMap<>();
        this.config = new HikariDataSource();
    }

    @Override
    public void setup() {

        // Check to see if the “plugins” folder exists; if it doesn't, create it.
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        // Hikaru is configured with WAL enabled and a 5-second timeout
        config.setJdbcUrl("jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db") + "?journal_mode=WAL");
        config.setConnectionTimeout(5000); // 5seg
        config.setMaximumPoolSize(5);
        initTables();

    }

    @Override
    public PlayerData getPlayerData(UUID id) {
        return temporalMemory.get(id);
    }

    @Override
    public void loadPlayer(UUID id) {
        PlayerData data = new PlayerData();
        temporalMemory.put(id, data);

        dbExecutor.execute(() -> {

            String queryPlayer = "SELECT equipped_skill FROM players WHERE uuid = ?;";
            String queryCooldowns = "SELECT skill_id, expiration_time FROM player_cooldowns WHERE uuid = ?;";

            // Local Variables
            final Skills[] loadedSkill = new Skills[1];
            Map<String, Long> loadedCooldowns = new HashMap<>();

            try (Connection conn = getConnection()) {
                // --- Skills block ---
                try (PreparedStatement stmt = conn.prepareStatement(queryPlayer)) {
                    stmt.setString(1, id.toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String savedSkill = rs.getString("equipped_skill");

                            if (savedSkill != null && !savedSkill.equals("none")) {
                                try {
                                    loadedSkill[0] = Skills.valueOf(savedSkill);
                                } catch (IllegalArgumentException e) {
                                    plugin.getLogger().warning("Invalid skill in DB for" + id + ": " + savedSkill);
                                }
                            }
                        }
                    }
                }

                // --- Cooldowns Block ---
                try (PreparedStatement stmt = conn.prepareStatement(queryCooldowns)) {
                    stmt.setString(1, id.toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        long currentTime = System.currentTimeMillis();
                        while (rs.next()) {
                            String skillKey = rs.getString("skill_id");
                            long expirationTime = rs.getLong("expiration_time");

                            if (!(currentTime < expirationTime)) continue;
                            loadedCooldowns.put(skillKey, expirationTime);
                        }
                    }

                }
            } catch (SQLException e) {
                plugin.getLogger().severe(e.getMessage());
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                PlayerData currentData = temporalMemory.get(id);
                if (currentData != null) {
                    currentData.setEquippedSkill(loadedSkill[0]);
                    currentData.getCooldowns().putAll(loadedCooldowns);
                }
            });
        });
    }

    @Override
    public void savePlayer(UUID id) {
        PlayerData data = temporalMemory.get(id);
        if (data == null) return;

        String skillName = (data.getEquippedSkill() != null) ? data.getEquippedSkill().name() : "none";
        Map<String, Long> cooldownsCopy = new HashMap<>(data.getCooldowns());

        dbExecutor.execute(() -> {
            String sqlPlayer = "INSERT OR REPLACE INTO players (uuid, equipped_skill) VALUES (?, ?);";
            String sqlCLearCooldowns = "DELETE FROM player_cooldowns WHERE uuid = ?;";
            String sqlInsertCooldowns = "INSERT OR REPLACE INTO player_cooldowns (uuid, skill_id, expiration_time) VALUES (?, ?, ?);";

            try(Connection conn = getConnection()) {
                conn.setAutoCommit(false); // One time

                try (PreparedStatement stmtPlayer = conn.prepareStatement(sqlPlayer)) {
                    stmtPlayer.setString(1, id.toString());
                    stmtPlayer.setString(2, skillName);

                    stmtPlayer.executeUpdate();
                }

                try (PreparedStatement stmtClear = conn.prepareStatement(sqlCLearCooldowns)) {
                    stmtClear.setString(1, id.toString());
                    stmtClear.executeUpdate();
                }

                long currentTime = System.currentTimeMillis();
                try (PreparedStatement stmtCooldowns = conn.prepareStatement(sqlInsertCooldowns)) {
                    for (Map.Entry<String, Long> entry : cooldownsCopy.entrySet()) {
                        long expiration = entry.getValue();

                        if (currentTime < expiration) {
                            stmtCooldowns.setString(1, id.toString());
                            stmtCooldowns.setString(2, entry.getKey());
                            stmtCooldowns.setLong(3, expiration);

                            stmtCooldowns.addBatch();
                        }
                    }

                    stmtCooldowns.executeBatch();
                }

                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.getLogger().severe(e.getMessage());
            }
        });
    }

    @Override
    public void saveAll(boolean clearCache) {
        for (UUID id : new HashMap<>(temporalMemory).keySet()) { savePlayer(id); }
        if (clearCache) {
            temporalMemory.clear();
        }
    }

    @Override
    public void unloadPlayer(UUID id) {
        temporalMemory.remove(id);
    }

    private void initTables() {
        final String sqlPlayersTable = "CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY NOT NULL, equipped_skill TEXT);";
        final String sqlPlayerCooldownsTable = "CREATE TABLE IF NOT EXISTS player_cooldowns (uuid TEXT, skill_id TEXT NOT NULL, expiration_time INTEGER NOT NULL, PRIMARY KEY(uuid, skill_id));";

        try(final Connection conn = getConnection();
            final PreparedStatement stmtPlayers = conn.prepareStatement(sqlPlayersTable);
            final PreparedStatement stmtCooldowns = conn.prepareStatement(sqlPlayerCooldownsTable)) {

            stmtPlayers.executeUpdate();
            stmtCooldowns.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return this.config.getConnection();
    }

    public void shutdown() {
        plugin.getLogger().info("Shutting down the database service safely...");
        dbExecutor.shutdown();

        try {
            if (!dbExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                dbExecutor.shutdownNow();
            }

        } catch (InterruptedException e) {
            dbExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (config != null && !config.isClosed()) {
            config.close();
        }
    }
}
