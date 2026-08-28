package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A simple {@link java.util.function.Function} that can throw a {@link SoulBoundException}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param arg the function argument
     * @return the function result
     * @throws SoulBoundException if the function execution fails
     * @since 2.0.0
     */
    R apply(T arg) throws SoulBoundException;
}
