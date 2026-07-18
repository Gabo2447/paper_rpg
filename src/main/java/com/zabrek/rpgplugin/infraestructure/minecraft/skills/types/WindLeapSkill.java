package com.zabrek.rpgplugin.infraestructure.minecraft.skills.types;

import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillActive;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

public class WindLeapSkill extends SkillActive {

    @Override
    public Skills getSkillType() { return Skills.WIND_LEAP; }

    @Override
    public void onDamage(EntityDamageEvent event, Player player) {
        if (event.getCause() == DamageCause.FALL) {
            event.setCancelled(true);
            player.sendMessage(Component.text("Your wind skill saved you!", NamedTextColor.DARK_AQUA));
        }
    }

    @Override
    protected boolean executeActiveEffect(Player player, PlayerData playerData) {
        if (player.isGliding()) {
            player.sendMessage(Component.text("You can't use your ability while gliding!", NamedTextColor.RED));
            return false;
        }

        Vector currentVelocity = player.getLocation().getDirection();
        Vector impulseJump = currentVelocity.multiply(1.5);
        impulseJump.setY(0.8);

        Vector finalVelocity = currentVelocity.add(impulseJump);
        player.setVelocity(finalVelocity);
        return true;
    }
}
