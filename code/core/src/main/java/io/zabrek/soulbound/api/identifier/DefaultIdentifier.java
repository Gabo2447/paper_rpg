package io.zabrek.soulbound.api.identifier;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Identifiers are used to identify objects in SoulBound.
 */
public abstract class DefaultIdentifier implements Identifier {

    /**
     * The type of the object.
     */
    private final String type;

    /**
     * The unique key of the object within its type.
     */
    private final String key;

    /**
     * Creates a new default identifier.
     *
     * @param type the type of the object
     * @param key  the unique key of the object
     */
    protected DefaultIdentifier(final String type, final String key) {
        this.type = type;
        this.key = key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getFull() {
        return type + SEPARATOR + key;
    }

    @Override
    public String toString() {
        return getFull();
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DefaultIdentifier other = (DefaultIdentifier) obj;
        return Objects.equals(type, other.type)
                && Objects.equals(key, other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key);
    }
}
