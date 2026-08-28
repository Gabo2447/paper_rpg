package io.zabrek.soulbound.api.kernel;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Manages and loads the core components.
 *
 * @since 1.0.0
 */
public interface CoreComponentLoader {

    /**
     * Register a core component into the loading queue.
     *
     * @param component the core component to register
     * @since 1.0.0
     */
    void register(CoreComponent component);

    /**
     * Initializes an initial instance into the dependency provider.
     *
     * @param clazz    the class type of the dependency
     * @param instance the concrete instance to initialize
     * @param <T>      the generic type of the instance
     * @since 1.0.0
     */
    <T> void init(Class<T> clazz, T instance);

    /**
     * Get a loaded instance by its type.
     * Will ignore multiple instances of the same type and just return the first one to find.
     *
     * @param type the type of the instance to get
     * @param <T>  the type of the instance
     * @return the loaded instance
     * @throws NoSuchElementException if no instance of the given type was found
     * @since 2.0.0
     */
    <T> T get(Class<T> type);

    /**
     * Get a loaded instance by its type wrapped in an {@link Optional}.
     * Will ignore multiple instances of the same type and just return the first one to find.
     * <br>
     * Won't throw an exception if no instance of the given type was found but will instead return an empty optional.
     *
     * @param type the type of the instance to get
     * @param <T>  the type of the instance
     * @return the loaded instance wrapped in an optional or an empty optional if no instance was found
     * @since 2.0.0
     */
    <T> Optional<T> getOptional(Class<T> type);

    /**
     * Get all loaded instances matching a given type.
     *
     * @param type the type of the instances to get
     * @param <T>  the type of the instances
     * @return a collection of loaded instances that may be empty
     * @since 2.0.0
     */
    <T> Collection<T> getAll(Class<T> type);

    /**
     * Validates dependencies and sequentially loads all registered components.
     * <br><br>
     * For each component, it verifies that all required dependencies are present
     * in the provider before invoking its loading logic.
     *
     * @throws IllegalStateException if a component requires a dependency that has not been provided
     * @since 1.0.0
     */
    void load();
}
