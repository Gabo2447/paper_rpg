package com.zabrek.rpgplugin.infraestructure.minecraft.skills;

import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.Skills;

import com.zabrek.rpgplugin.domain.model.SkillProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public interface SkillBehavior {
    Skills getSkillType();
    void onDamage(EntityDamageEvent event, Player player);
    void onCommandExecute(Player player, PlayerData playerData, SkillProperties props);
}
