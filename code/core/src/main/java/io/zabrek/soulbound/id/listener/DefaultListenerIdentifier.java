package io.zabrek.soulbound.id.listener;

import io.zabrek.soulbound.api.identifier.DefaultIdentifier;
import io.zabrek.soulbound.api.identifier.ListenerIdentifier;

/**
 * The default implementation of {@link ListenerIdentifier}s.
 */
public class DefaultListenerIdentifier extends DefaultIdentifier implements ListenerIdentifier {

    /**
     * Creates a new default listener identifier.
     *
     * @param type the listener type (e.g., "damage", "block")
     * @param key  the unique key of the listener instance
     */
    public DefaultListenerIdentifier(final String type, final String key) {
        super(type, key);
    }
}
