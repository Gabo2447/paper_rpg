package io.zabrek.soulbound.api.logger;

import org.jetbrains.annotations.Nullable;

/**
 * A provider of a log source's path.
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface LogSource {

    /**
     * An empty {@link LogSource} pointing to no source at all.
     * The {@link #getSourcePath()} method will always return {@code null}.
     *
     * @since 2.0.0
     */
    LogSource EMPTY = () -> null;

    /**
     * Gets the path that addresses this {@link LogSource}.
     *
     * @return the address
     * @since 2.0.0
     */
    @Nullable
    String getSourcePath();
}
