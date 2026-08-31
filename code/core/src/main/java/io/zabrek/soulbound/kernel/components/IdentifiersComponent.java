package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.kernel.registry.soul.IdentifierTypeRegistry;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link IdentifierTypeRegistry}.
 */
public class IdentifiersComponent implements CoreComponent {

    /**
     * Create a new IdentifiersComponent.
     */
    public IdentifiersComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(SoulBoundLoggerFactory.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(IdentifierTypeRegistry.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final SoulBoundLoggerFactory loggerFactory = provider.get(SoulBoundLoggerFactory.class);
        final IdentifierTypeRegistry identifierTypeRegistry = new IdentifierTypeRegistry(loggerFactory.create(IdentifierTypeRegistry.class));

        provider.take(IdentifierTypeRegistry.class, identifierTypeRegistry);
    }
}
