package io.zabrek.soulbound.id.listener;

import io.zabrek.soulbound.api.identifier.ListenerIdentifier;
import io.zabrek.soulbound.api.identifier.factory.DefaultIdentifierFactory;

/**
 * A {@link DefaultIdentifierFactory} for {@link ListenerIdentifier}.
 */
public class ListenerIdentifierFactory extends DefaultIdentifierFactory<ListenerIdentifier> {

    /**
     * Creates a new listener identifier factory.
     */
    public ListenerIdentifierFactory() {
        super("Listener");
    }

    @Override
    public ListenerIdentifier createIdentifier(final String type, final String key) {
        return new DefaultListenerIdentifier(type, key);
    }
}
