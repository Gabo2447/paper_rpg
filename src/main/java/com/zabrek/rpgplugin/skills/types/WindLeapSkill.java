package com.zabrek.rpgplugin.skills.types;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.skills.SkillBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class WindLeapSkill implements SkillBehavior {
    @Override
    public Skills getSkillType() { return Skills.WIND_LEAP; }

    @Override
    public void onDamage(EntityDamageEvent event, Player player) {
        if (event.getCause() == DamageCause.FALL) {
            event.setCancelled(true);
            player.sendMessage(Component.text("Your wind skill saved you!", NamedTextColor.DARK_AQUA));
        }
    }
}
