package com.zabrek.rpgplugin.infraestructure.minecraft.skills;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.SkillProperties;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.types.*;

import java.util.HashMap;
import java.util.Map;

public class SkillRegistry {
    private final Map<Skills, SkillBehavior> behaviors = new HashMap<>();
    private final Map<Skills, SkillProperties> properties = new HashMap<>();

    public SkillRegistry() {
        register(new SeismicImpactSkill());
        register(new StreakOfGoodLuckSkill());
        register(new AdrenalineInTheBloodSkill());
        register(new PiercingArrow());
        register(new ObsidianSkin());

        properties.put(Skills.SEISMIC_IMPACT, new SkillProperties(15, 10)); // PASSIVE
        properties.put(Skills.STREAK_OF_GOOD_LUCK, new SkillProperties(20, 10)); // PASSIVE
        properties.put(Skills.ADRENALINE_IN_THE_BLOOD, new SkillProperties(30, 5));
        properties.put(Skills.PIERCING_ARROW, new SkillProperties(10, 20));
        properties.put(Skills.OBSIDIAN_SKIN, new SkillProperties(40, 0));
    }

    private void register(SkillBehavior behavior) {
        behaviors.put(behavior.getSkillType(), behavior);
    }

    public SkillBehavior getBehavior(Skills skill) { return behaviors.get(skill); }
    public SkillProperties getProperties(Skills skill) { return properties.get(skill); }
}