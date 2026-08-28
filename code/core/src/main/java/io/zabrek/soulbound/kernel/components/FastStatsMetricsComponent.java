package io.zabrek.soulbound.kernel.components;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.faststats.FastStatsMetrics;
import io.zabrek.soulbound.faststats.FastStatsMetricsProvider;
import io.zabrek.soulbound.kernel.DependencyProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * The implementation of {@link CoreComponent} for {@link FastStatsMetrics}.
 */
public class FastStatsMetricsComponent implements CoreComponent {

    /**
     * The token to use for metrics publication to FastStats.
     * According to FastStats' documentation, this token is safe for shipping with the plugin's code.
     */
    private static final String TOKEN = "83837c01802c008bf5acd7e3cc4d87d4";

    /**
     * Create a new FastStatsMetricsComponent instance.
     */
    public FastStatsMetricsComponent() {
    }

    @Override
    public Set<Class<?>> requires() {
        return Set.of(JavaPlugin.class, PluginDescriptionFile.class);
    }

    @Override
    public Set<Class<?>> provides() {
        return Set.of(FastStatsMetrics.class);
    }

    @Override
    public void load(final DependencyProvider provider) {
        final JavaPlugin plugin = provider.get(JavaPlugin.class);
        final PluginDescriptionFile description = provider.get(PluginDescriptionFile.class);

        final Set<FastStatsMetricsProvider> fastStatsMetricsProviders = (Set<FastStatsMetricsProvider>) provider.getAll(FastStatsMetricsProvider.class);
        final FastStatsMetrics fastStatsMetrics = new FastStatsMetrics(plugin, TOKEN, fastStatsMetricsProviders, true);
        fastStatsMetrics.enable();

        provider.take(FastStatsMetrics.class, fastStatsMetrics);
    }
}
