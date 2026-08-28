package io.zabrek.soulbound.api.common.function;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * A simple {@link java.util.function.Supplier} that can throw a {@link SoulBoundException}.
 *
 * @param <T> the type of results supplied by this supplier
 * @since 2.0.0
 */
@FunctionalInterface
public interface SoulBoundSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws SoulBoundException when the method execution fails
     * @since 2.0.0
     */
    T get() throws SoulBoundException;
}
