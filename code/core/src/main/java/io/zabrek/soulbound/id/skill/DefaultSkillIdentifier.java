package io.zabrek.soulbound.id.skill;

import io.zabrek.soulbound.api.identifier.DefaultIdentifier;
import io.zabrek.soulbound.api.identifier.SkillIdentifier;

/**
 * The default implementation of {@link SkillIdentifier}.
 */
public class DefaultSkillIdentifier extends DefaultIdentifier implements SkillIdentifier {

    /**
     * Creates a new default listener identifier.
     *
     * @param type the listener type (e.g., "damage", "block")
     * @param key  the unique key of the listener instance
     */
    public DefaultSkillIdentifier(final String type, final String key) {
        super(type, key);
    }
}
