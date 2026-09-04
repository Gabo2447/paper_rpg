package io.zabrek.soulbound.api.data;

import io.zabrek.soulbound.api.identifier.SkillIdentifier;

/**
 * The cooldown data.
 *
 * @param skill the skill identifier
 * @param time  the expired time.
 */
public record CooldownRecord(SkillIdentifier skill, String time) {

}
