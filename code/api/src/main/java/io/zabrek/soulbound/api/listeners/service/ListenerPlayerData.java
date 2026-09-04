package io.zabrek.soulbound.api.listeners.service;

import io.zabrek.soulbound.api.data.CooldownRecord;
import io.zabrek.soulbound.api.data.LevelRecord;

import java.util.List;

/**
 * The listener data from player.
 *
 * @since 2.0.0
 */
public interface ListenerPlayerData {

    /**
     * Get the player level data.
     *
     * @return player level data
     * @since 2.0.0
     */
    List<LevelRecord> getLevel();

    /**
     * Get the player cooldown data.
     *
     * @return player cooldown
     * @since 2.0.0
     */
    List<CooldownRecord> getCooldown();
}
