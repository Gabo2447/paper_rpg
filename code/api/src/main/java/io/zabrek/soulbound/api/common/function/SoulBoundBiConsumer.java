package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

import java.util.function.BiConsumer;

/**
 * A simple {@link BiConsumer} that can throw a {@link SoulBoundException}.
 *
 * @param <T> the type of the first input to the operation
 * @param <U> the type of the second input to the operation
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundBiConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param firstArg  the first input argument
     * @param secondArg the second input argument
     * @throws SoulBoundException when the method execution fails
     * @since 2.0.0
     */
    void accept(T firstArg, U secondArg) throws SoulBoundException;
}
