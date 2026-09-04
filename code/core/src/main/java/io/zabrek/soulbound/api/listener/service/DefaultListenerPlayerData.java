package io.zabrek.soulbound.api.listener.service;

import io.zabrek.soulbound.api.data.CooldownRecord;
import io.zabrek.soulbound.api.data.LevelRecord;
import io.zabrek.soulbound.api.listeners.service.ListenerPlayerData;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation for {@link ListenerPlayerData}.
 */
public class DefaultListenerPlayerData implements ListenerPlayerData {

    /**
     * List of level's record.
     */
    private final List<LevelRecord> levelRecord;

    /**
     * List of cooldown's record.
     */
    private final List<CooldownRecord> cooldownRecord;

    /**
     * Creates a new DTO.
     *
     * @param levelRecord the level for this profile
     * @param cooldownRecord the cooldowns for this profile
     */
    public DefaultListenerPlayerData(final List<LevelRecord> levelRecord, final List<CooldownRecord> cooldownRecord) {
        this.levelRecord = levelRecord;
        this.cooldownRecord = cooldownRecord;
    }

    @Override
    public List<LevelRecord> getLevel() {
        return Collections.unmodifiableList(levelRecord);
    }

    @Override
    public List<CooldownRecord> getCooldown() {
        return Collections.unmodifiableList(cooldownRecord);
    }
}
