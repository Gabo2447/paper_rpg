package io.zabrek.soulbound.id.trigger;

import io.zabrek.soulbound.api.identifier.DefaultIdentifier;
import io.zabrek.soulbound.api.identifier.TriggerIdentifier;

/**
 * The default implementation of {@link TriggerIdentifier}s.
 */
public class DefaultTriggerIdentifier extends DefaultIdentifier implements TriggerIdentifier {

    /**
     * Creates a new default trigger identifier.
     *
     * @param type the trigger type (e.g., "damage", "block")
     * @param key  the unique key of the trigger instance
     */
    public DefaultTriggerIdentifier(final String type, final String key) {
        super(type, key);
    }
}
