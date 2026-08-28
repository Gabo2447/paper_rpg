package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A simple {@link java.lang.Runnable} that can throw a {@link SoulBoundException}.
 *
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundRunnable {

    /**
     * Executes the runnable.
     *
     * @throws SoulBoundException if the runnable fails
     * @since 2.0.0
     */
    void run() throws SoulBoundException;
}
