package io.zabrek.soulbound.listeners.death;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.listeners.Listener;
import io.zabrek.soulbound.api.listeners.ListenerFactory;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * The default implementation for {@link ListenerFactory}.
 */
public class EntityDeathFactory implements ListenerFactory {

    /**
     * Creates a new EntityDeathFactory.
     */
    public EntityDeathFactory() {
    }

    @Override
    public Listener create(final ListenerService service) throws SoulBoundException {
        final EntityDeath entityDeath = new EntityDeath(service);
        service.request(EntityDeathEvent.class)
                .player(event -> event.getEntity().getKiller() instanceof final Player player ? player : null)
                .onlineHandler(entityDeath::onDeath)
                .priority(EventPriority.HIGHEST)
                .subscribe(true);
        return entityDeath;
    }
}
