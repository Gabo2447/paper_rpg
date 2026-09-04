package io.zabrek.soulbound.listeners.death;

import io.zabrek.soulbound.api.DefaultListener;
import io.zabrek.soulbound.api.data.LevelRecord;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import io.zabrek.soulbound.api.profile.OnlineProfile;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The listener to watch the death event entity.
 */
public class EntityDeath extends DefaultListener {

    /**
     * Creates a new instance of the listener.
     *
     * @param service the {@link ListenerService} for this listener.
     */
    public EntityDeath(final ListenerService service) {
        super(service);
    }

    /**
     * The handler to watch if the player kills a mob.
     *
     * @param event   the event
     * @param profile the profile
     */
    public void onDeath(final EntityDeathEvent event, final OnlineProfile profile) {
        final List<LevelRecord> data = service.getData().getLevels(profile);

        final List<LevelRecord> newData = new ArrayList<>();
        data.forEach(level -> newData.add(calcLevel(level, event)));
        service.getData().updateLevel(profile, newData);
    }

    private LevelRecord calcLevel(final LevelRecord record, final EntityDeathEvent event) {
        double experience = calcExpToAdd(event.getDroppedExp(), event, record) + record.experience();
        int level = record.level();

        while (true) {
            final double nextLevelReq = 100 * Math.pow(level, 1.5);
            if (experience < nextLevelReq) {
                break;
            }
            experience -= nextLevelReq;
            level++;
        }

        return new LevelRecord(record.skill(), level, experience);
    }

    private double calcExpToAdd(final double xp, final EntityDeathEvent event, final LevelRecord record) {
        if (xp <= 0) {
            return 0;
        }

        final double multiplier = MobComplexity.getComplexityOf(event.getEntityType());
        return xp * ((1 + multiplier) * (Math.min(10, record.level()))); // <- Change it before implement the mobs levels to apply: MOB_LEVEL - PLAYER_LEVEL
    }
}
