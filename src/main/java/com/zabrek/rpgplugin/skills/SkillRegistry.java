package com.zabrek.rpgplugin.skills;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.skills.types.*;

import java.util.HashMap;
import java.util.Map;

public class SkillRegistry {
    private final Map<Skills, SkillBehavior> skills = new HashMap<>();

    public SkillRegistry() {
        register(new LavaLeatherSkill());
        register(new WindLeapSkill());
    }

    private void register(SkillBehavior behavior) {
        skills.put(behavior.getSkillType(), behavior);
    }

    public SkillBehavior getBehavior(Skills skill) {
        return skills.get(skill);
    }
}
