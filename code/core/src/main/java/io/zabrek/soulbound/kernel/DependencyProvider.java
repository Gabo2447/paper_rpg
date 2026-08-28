package io.zabrek.soulbound.kernel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Manages the registration and retrieval of component dependencies.
 *
 * @since 2.0.0
 */
public class DependencyProvider {

    /**
     * Internal registry mapping dependency.
     */
    private final Map<Class<?>, Object> registry = new HashMap<>();

    /**
     * Default constructor without arguments.
     */
    public DependencyProvider() {
    }

    /**
     * Registers an instance associated with a specific class type into the provider.
     *
     * @param clazz    the class type identifying the dependency
     * @param instance the concrete implementation instance to store
     * @param <T>      the generic type of the dependency
     * @since 2.0.0
     */
    public <T> void take(final Class<T> clazz, final T instance) {
        registry.put(clazz, instance);
    }

    /**
     * Retrieves the instance associated with the specified class type.
     *
     * @param clazz the class type of the dependency to retrieve
     * @param <T>   the generic type of the dependency
     * @return the registered dependency instance
     * @throws IllegalStateException if the dependency is not found in the registry
     * @since 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> clazz) {
        final T instance = (T) registry.get(clazz);
        if (instance == null) {
            throw new NoSuchElementException("Dependency not found. " + clazz.getName() + "!");
        }
        return instance;
    }

    /**
     * Get all loaded instances matching a given type (exact, subclass, or interface).
     *
     * @param type the type of the instances to get
     * @param <T>  the type of the instances
     * @return a collection of loaded instances that may be empty
     * @since 2.0.0
     */
    public <T> Collection<T> getAll(final Class<T> type) {
        return registry.values().stream()
                .filter(instance -> type.isAssignableFrom(instance.getClass()))
                .map(type::cast)
                .collect(Collectors.toSet());
    }
}