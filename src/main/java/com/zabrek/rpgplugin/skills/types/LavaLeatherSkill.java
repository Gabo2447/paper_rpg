package com.zabrek.rpgplugin.skills.types;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.skills.SkillBehavior;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class LavaLeatherSkill implements SkillBehavior {
    @Override
    public Skills getSkillType() { return Skills.LAVA_LEATHER; }

    @Override
    public void onDamage(EntityDamageEvent event, Player player) {
        switch (event.getCause()) {
            case LAVA, FIRE_TICK, FIRE -> {
                if (ThreadLocalRandom.current().nextInt(4) == 0) {
                    event.setCancelled(true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0)); // Fire Resistance 1
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 4)); // Strength 5
                }
            }
        }
    }
}
