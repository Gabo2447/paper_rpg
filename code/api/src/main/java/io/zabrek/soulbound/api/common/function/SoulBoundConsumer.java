package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A simple {@link java.util.function.Consumer} that can throw a {@link SoulBoundException}.
 *
 * @param <T> the type of the input to the operation
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param arg the input argument
     * @throws SoulBoundException when the method execution fails
     * @since 2.0.0
     */
    void accept(T arg) throws SoulBoundException;
}
