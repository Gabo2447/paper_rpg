package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A {@link java.util.concurrent.Callable} that may throw a {@link SoulBoundException}.
 *
 * @param <R> the result type of the method call
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundCallable<R> {

    /**
     * Calls the method and gets the result.
     *
     * @return result of the check
     * @throws SoulBoundException when the method execution fails
     * @since 2.0.0
     */
    R call() throws SoulBoundException;
}
