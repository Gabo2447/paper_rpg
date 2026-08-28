package io.zabrek.soulbound.kernel;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.kernel.components.FastStatsMetricsComponent;
import io.zabrek.soulbound.kernel.components.ProfileProviderComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Factory utility class responsible for instantiating and grouping
 * the default core components of the SoulBound plugin.
 *
 * @since 2.0.0
 */
public final class SoulBoundComponents {

    private SoulBoundComponents() {
    }

    /**
     * Creates and returns a set containing all default core components.
     *
     * @param plugin the main JavaPlugin instance
     * @return a set of default core components
     * @since 1.0.0
     */
    public static Set<CoreComponent> createDefaults(final JavaPlugin plugin) {
        return Stream.of(
                createEssentials(),
                createFeatures(),
                createAdditionalFeatures()
        ).flatMap(Set::stream).collect(Collectors.toSet());
    }

    private static Set<CoreComponent> createEssentials() {
        return Set.of(
                new ProfileProviderComponent()
        );
    }

    private static Set<CoreComponent> createAdditionalFeatures() {
        return Set.of(
                new FastStatsMetricsComponent()
        );
    }

    private static Set<CoreComponent> createFeatures() {
        return Set.of();
    }
}