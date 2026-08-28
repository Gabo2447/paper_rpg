package com.zabrek.rpgplugin.application.usecases;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class AddSkillExperienceUseCase {
    private final PlayerRepository playerRepository;

    public AddSkillExperienceUseCase(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public SkillProgress execute(UUID playerID, int baseExperience, int mobLevel) {
        if (baseExperience <= 0 ) return null;
        baseExperience = Math.min(baseExperience, 500);

        PlayerData data = playerRepository.getPlayerData(playerID);
        if (data == null) return null;

        Skills equippedSkill = data.getEquippedSkill();
        if (equippedSkill == null) return null;

        System.out.println("DEBUG -> XP Base: " + baseExperience + " | Mob Level: " + mobLevel);
        System.out.println("DEBUG -> Skill Equipada: " + data.getEquippedSkill());

        // XP * MOB_LEVEL
        double finalXP = calculateXP(baseExperience, mobLevel); // Before implement mobs level change the formula with = XP * (1 + MULTIPLIER*(MOB_LEVEL-PLAYER_LEVEL))
        data.addSkillExperience(equippedSkill, finalXP);

        return data.getSkillProgress(equippedSkill);
    }

    public int calculateXP(int base, int mobLevel) {
        return base * (mobLevel * mobLevel); // XP * MobLvL^2
    }
}
