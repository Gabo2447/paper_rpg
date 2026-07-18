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
        register(new LavaLeatherSkill());
        register(new WindLeapSkill());

        properties.put(Skills.LAVA_LEATHER, new SkillProperties(15, 10));
        properties.put(Skills.WIND_LEAP, new SkillProperties(20, 10));
    }

    private void register(SkillBehavior behavior) {
        behaviors.put(behavior.getSkillType(), behavior);
    }

    public SkillBehavior getBehavior(Skills skill) { return behaviors.get(skill); }
    public SkillProperties getProperties(Skills skill) { return properties.get(skill); }
}