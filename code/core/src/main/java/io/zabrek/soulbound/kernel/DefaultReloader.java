package io.zabrek.soulbound.kernel;

import io.zabrek.soulbound.api.logger.SoulBoundLogger;
import io.zabrek.soulbound.api.reload.ReloadPhase;
import io.zabrek.soulbound.api.reload.Reloader;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Default implementation of {@link Reloader}.
 */
public class DefaultReloader implements Reloader {

    /**
     * Contains all registered {@link Runnable}s.
     */
    private final Map<ReloadPhase, Collection<Runnable>> reloadableMap;

    /**
     * The logger to use.
     */
    private final SoulBoundLogger log;

    /**
     * Create a new DefaultReloader.
     *
     * @param log the logger to use
     */
    public DefaultReloader(final SoulBoundLogger log) {
        this.log = log;
        this.reloadableMap = new EnumMap<>(ReloadPhase.class);
    }

    @Override
    public void register(final ReloadPhase reloadPhase, final Runnable reloadable) {
        reloadableMap.computeIfAbsent(reloadPhase, phase -> new HashSet<>()).add(reloadable);
        log.debug("Registered reloadable for phase " + reloadPhase);
    }

    @Override
    public void reload() {
        log.debug("Reloading %s registered reload-runnables".formatted(reloadableMap.values().stream().mapToInt(Collection::size).sum()));
        for (final ReloadPhase phase : ReloadPhase.values()) {
            final Collection<Runnable> runnable = reloadableMap.getOrDefault(phase, Collections.emptyList());
            log.debug("Running %s functions for phase %s".formatted(runnable.size(), phase));
            runnable.forEach(Runnable::run);
        }
        log.debug("Reload finished");
    }
}
