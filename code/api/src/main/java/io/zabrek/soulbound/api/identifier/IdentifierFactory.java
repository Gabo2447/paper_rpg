package io.zabrek.soulbound.api.identifier;

import io.zabrek.soulbound.api.SoulBoundException;

/**
 * Factory for {@link Identifier} instances.
 *
 * @param <I> the type of the identifier
 * @since 2.0.0
 */
@FunctionalInterface
public interface IdentifierFactory<I extends Identifier> {

    /**
     * Parses an identifier from a string.
     *
     * @param input the input string
     * @return the parsed identifier
     * @throws SoulBoundException if the identifier cannot be parsed
     * @since 2.0.0
     */
    I parseIdentifier(String input) throws SoulBoundException;
}
