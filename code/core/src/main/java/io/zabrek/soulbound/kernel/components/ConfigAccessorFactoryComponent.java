package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.config.ConfigAccessorFactory;
import io.zabrek.soulbound.api.kernel.CoreComponent;
// import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.lib.config.DefaultConfigAccessorFactory;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link ConfigAccessorFactory}.
 */
public class ConfigAccessorFactoryComponent implements CoreComponent {

    /**
     * Create a new ConfigAccessorFactoryComponent.
     */
    public ConfigAccessorFactoryComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(/*SoulBoundLoggerFactory.class*/);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(DefaultConfigAccessorFactory.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final DefaultConfigAccessorFactory configAccessorFactory = new DefaultConfigAccessorFactory();
        provider.take(DefaultConfigAccessorFactory.class, configAccessorFactory);
    }
}
