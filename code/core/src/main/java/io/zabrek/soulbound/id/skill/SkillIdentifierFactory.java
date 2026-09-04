package io.zabrek.soulbound.id.skill;

import io.zabrek.soulbound.api.identifier.SkillIdentifier;
import io.zabrek.soulbound.api.identifier.factory.DefaultIdentifierFactory;

/**
 * A {@link DefaultIdentifierFactory} for {@link SkillIdentifier}.
 */
public class SkillIdentifierFactory extends DefaultIdentifierFactory<SkillIdentifier> {

    /**
     * Creates a new skill identifier factory.
     */
    public SkillIdentifierFactory() {
        super("Skill");
    }

    @Override
    public SkillIdentifier createIdentifier(final String type, final String key) {
        return new DefaultSkillIdentifier(type, key);
    }
}
