package io.zabrek.soulbound.listeners.join;

import io.zabrek.soulbound.api.DefaultListener;
import io.zabrek.soulbound.api.listeners.service.ListenerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listen the player join event.
 */
public class PlayerJoin extends DefaultListener {

    /**
     * Creates a new instance of the listener.
     *
     * @param service the {@link ListenerService} for this listener.
     */
    public PlayerJoin(final ListenerService service) {
        super(service);
    }

    /**
     * The handler to listen the player join event.
     *
     * @param event the event to get information.
     */
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.sendMessage(Component.text("[+] ", NamedTextColor.GREEN)
                .append(Component.text("The player ", NamedTextColor.GRAY))
                .append(Component.text(player.getName(), NamedTextColor.GREEN))
                .append(Component.text(" joined.", NamedTextColor.GRAY)));

        player.sendMessage(Component.text("TIP: Try using the “/skills” command to select your class", NamedTextColor.GOLD));
        player.sendMessage(Component.text("If you're going to select your class, you can use the “/use” command."));
    }
}
