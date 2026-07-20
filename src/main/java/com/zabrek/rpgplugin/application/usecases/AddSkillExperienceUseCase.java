package com.zabrek.rpgplugin.application.usecases;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.domain.model.PlayerData;

import java.util.UUID;

public class AddSkillExperienceUseCase {
    private final PlayerRepository playerRepository;

    public AddSkillExperienceUseCase(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public SkillProgress execute(UUID playerID, int baseExperience) {
        if (baseExperience <= 0 ) return null;

        PlayerData data = playerRepository.getPlayerData(playerID);
        if (data == null) return null;

        Skills equippedSkill = data.getEquippedSkill();
        if (equippedSkill == null) return null;

        double finalXP = baseExperience * 1.5; // Before implement mobs level change the formula with = XP * (1 + MULTIPLIER*(MOB_LEVEL-PLAYER_LEVEL))
        data.addSkillExperience(equippedSkill, finalXP);

        return data.getSkillProgress(equippedSkill);
    }
}
