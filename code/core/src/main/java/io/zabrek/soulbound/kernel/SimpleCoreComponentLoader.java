package io.zabrek.soulbound.kernel;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.kernel.CoreComponentLoader;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * Manages and loads the core components.
 *
 * @since 1.0.0
 */
public class SimpleCoreComponentLoader implements CoreComponentLoader {

    /**
     * The ordered set of registered core components.
     */
    private final Set<CoreComponent> components = new LinkedHashSet<>();

    /**
     * The central provider.
     */
    private final DependencyProvider provider = new DependencyProvider();

    /**
     * Instances a new SimpleCoreComponentLoader.
     */
    public SimpleCoreComponentLoader() {
    }

    @Override
    public void register(final CoreComponent component) {
        components.add(component);
    }

    @Override
    public <T> void init(final Class<T> type, final T instance) {
        provider.take(type, instance);
    }

    @Override
    public <T> T get(final Class<T> type) {
        return provider.get(type);
    }

    @Override
    public <T> Optional<T> getOptional(final Class<T> type) {
        try {
            return Optional.ofNullable(provider.get(type));
        } catch (final NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Collection<T> getAll(final Class<T> type) {
        return provider.getAll(type);
    }

    @Override
    public void load() {
        for (final CoreComponent component : components) {
            for (final Class<?> req : component.requires()) {
                if (provider.get(req) == null) {
                    throw new IllegalStateException("Component " + component + " requires " + req + " but it was not found.");
                }
            }
            component.load(provider);
        }
    }
}
