package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A simple {@link java.util.function.BiFunction} that can throw a {@link SoulBoundException}.
 *
 * @param <T> the type of the first input to the function
 * @param <U> the type of the second input to the function
 * @param <R> the type of the result of the function
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param firstArg  the first function argument
     * @param secondArg the second function argument
     * @return the function result
     * @throws SoulBoundException if the function execution fails
     * @since 2.0.0
     */
    R apply(T firstArg, U secondArg) throws SoulBoundException;
}
