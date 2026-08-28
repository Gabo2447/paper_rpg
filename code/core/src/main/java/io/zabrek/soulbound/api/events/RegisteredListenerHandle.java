package io.zabrek.soulbound.api.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Represents a handle to a registered listener.
 */
public class RegisteredListenerHandle implements Subscription {

    /**
     * The Bukkit listener associated with this handle.
     */
    private final Listener listener;

    /**
     * Constructs a new RegisteredListenerHandle.
     *
     * @param listener the listener to manage
     */
    public RegisteredListenerHandle(final Listener listener) {
        this.listener = listener;
    }

    @Override
    public void unsubscribe() {
        HandlerList.unregisterAll(listener);
    }
}
