package io.zabrek.soulbound.api.identifier.factory;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.identifier.Identifier;
import io.zabrek.soulbound.api.identifier.IdentifierFactory;

/**
 * A default implementation of {@link IdentifierFactory} for flat identifiers.
 *
 * @param <I> the type of identifier to create
 * @since 2.0.0
 */
public abstract class DefaultIdentifierFactory<I extends Identifier> implements IdentifierFactory<I> {

    /**
     * The readable type name used for clean error logging.
     */
    private final String readableTypeName;

    /**
     * Creates a new identifier factory.
     *
     * @param readableTypeName the readable type name of the identifier
     */
    protected DefaultIdentifierFactory(final String readableTypeName) {
        this.readableTypeName = readableTypeName;
    }

    /**
     * Hook method implemented by subclasses to instantiate the specific identifier object.
     *
     * @param type the parsed type part
     * @param key  the parsed key part
     * @return the concrete identifier instance
     * @throws SoulBoundException if creation fails
     */
    protected abstract I createIdentifier(String type, String key) throws SoulBoundException;

    @Override
    public I parseIdentifier(final String input) throws SoulBoundException {
        validateInput(input);

        final String[] parts = input.split(Identifier.SEPARATOR, 2);
        final String type = parts[0].trim();
        final String key = parts[1].trim();

        validateParts(type, key, input);

        return createIdentifier(type, key);
    }

    private void validateInput(final String input) throws SoulBoundException {
        if (input.isBlank()) {
            throw new SoulBoundException("Empty identifier");
        }

        if (input.contains(" ")) {
            throw new SoulBoundException("Spaces are invalid for %s identifier '%s'".formatted(readableTypeName, input));
        }

        if (!input.contains(Identifier.SEPARATOR)) {
            throw new SoulBoundException("%s identifier '%s' is missing the separator '%s'!".formatted(readableTypeName, input, Identifier.SEPARATOR));
        }
    }

    private void validateParts(final String type, final String key, final String input) throws SoulBoundException {
        if (type.isBlank() || key.isBlank()) {
            throw new SoulBoundException("%s identifier '%s' has an empty type or key!".formatted(readableTypeName, input));
        }
    }
}
