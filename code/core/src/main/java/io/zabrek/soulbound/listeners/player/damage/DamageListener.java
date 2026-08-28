package io.zabrek.soulbound.listeners.player.damage;

import io.zabrek.soulbound.api.events.DefaultListener;
import io.zabrek.soulbound.api.events.EventListenerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener responsible for handling player join events.
 */
public class DamageListener extends DefaultListener {

    /**
     * Creates a new instance of the objective.
     *
     * @param service the {@link EventListenerService} for this objective
     */
    public DamageListener(final EventListenerService service) {
        super(service);
        this.subscription = service.request(EntityDamageEvent.class)
                .handler(this::onDamage)
                .priority(EventPriority.HIGHEST)
                .build();
    }

    private void onDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof final Player player) {

        }
    }
}
