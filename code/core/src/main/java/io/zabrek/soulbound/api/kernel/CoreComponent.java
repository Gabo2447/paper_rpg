package io.zabrek.soulbound.api.kernel;

import io.zabrek.soulbound.kernel.DependencyProvider;
import io.zabrek.soulbound.kernel.SimpleCoreComponentLoader;

import java.util.Set;

/**
 * A core component of the CoreMetrics plugin representing a unique unit of functionality that may be loaded
 * by a {@link SimpleCoreComponentLoader} respecting its dependencies and thereby being loaded in order.
 * <br><br>
 * Every {@link CoreComponent} defines a list of required dependencies in {@link #requires()} that must be available
 * via {@link DependencyProvider} before the component can be loaded, as well as a list of provided dependencies
 * in {@link #provides()} that are registered during the loading process.
 *
 * @since 2.0.0
 */
public interface CoreComponent {

    /**
     * Returns the set of classes representing the dependencies required by this component
     * to be present in the container before it can be loaded.
     *
     * @return a set of required dependency classes
     * @since 2.0.0
     */
    Set<Class<?>> requires();

    /**
     * Returns the set of classes representing the dependencies or services provided
     * and registered by this component during its loading phase.
     *
     * @return a set of provided dependency classes
     * @since 2.0.0
     */
    Set<Class<?>> provides();

    /**
     * Loads this component using the specified dependency provider.
     * <br><br>
     * During this execution, the component should initialize its internal logic and register
     * it's provided instances into the provider using {@link DependencyProvider#take(Class, Object)}.
     *
     * @param provider the dependency provider used to fetch requirements and register provided instances
     * @since 2.0.0
     */
    void load(DependencyProvider provider);
}