package com.zabrek.rpgplugin.skills;

import com.zabrek.rpgplugin.Skills;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public interface SkillBehavior {
    Skills getSkillType();
    void onDamage(EntityDamageEvent event, Player player);
}
