package io.zabrek.soulbound.api.data;

import io.zabrek.soulbound.api.identifier.SkillIdentifier;

/**
 * The player level data.
 *
 * @param skill      the skill
 * @param level      the level
 * @param experience the experience
 */
public record LevelRecord(SkillIdentifier skill, int level, double experience) {

}
