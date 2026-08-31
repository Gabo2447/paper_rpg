package io.zabrek.soulbound.id.trigger;

import io.zabrek.soulbound.api.identifier.TriggerIdentifier;
import io.zabrek.soulbound.api.identifier.factory.DefaultIdentifierFactory;

/**
 * A {@link DefaultIdentifierFactory} for {@link TriggerIdentifier}s.
 */
public class TriggerIdentifierFactory extends DefaultIdentifierFactory<TriggerIdentifier> {

    /**
     * Creates a new trigger identifier factory.
     */
    public TriggerIdentifierFactory() {
        super("Trigger");
    }

    @Override
    public TriggerIdentifier createIdentifier(final String type, final String key) {
        return new DefaultTriggerIdentifier(type, key);
    }
}
