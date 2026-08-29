package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.api.reload.Reloader;
import io.zabrek.soulbound.kernel.DefaultReloader;
import io.zabrek.soulbound.kernel.DependencyProvider;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link DefaultReloader}.
 */
public class ReloaderComponent implements CoreComponent {

    /**
     * Create a new ReloaderComponent.
     */
    public ReloaderComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(SoulBoundLoggerFactory.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(Reloader.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final SoulBoundLoggerFactory loggerFactory = provider.get(SoulBoundLoggerFactory.class);
        provider.take(Reloader.class, new DefaultReloader(loggerFactory.create(DefaultReloader.class)));
    }
}
