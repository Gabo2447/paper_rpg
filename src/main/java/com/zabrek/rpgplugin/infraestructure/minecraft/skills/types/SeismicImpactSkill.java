package com.zabrek.rpgplugin.infraestructure.minecraft.skills.types;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.model.SkillProgress;
import com.zabrek.rpgplugin.domain.model.SkillProperties;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillBehavior;
import com.zabrek.rpgplugin.infraestructure.minecraft.utils.VisualEffectUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Map;

public class SeismicImpactSkill implements SkillBehavior {
    @Override
    public Skills getSkillType() { return Skills.SEISMIC_IMPACT; }

    @Override
    public void onDamage(EntityDamageEvent event, Player player, PlayerData playerData) {
        switch (event.getCause()) {
            case FALL -> {
                event.setCancelled(true);
                Location center = player.getLocation();

                Map<String, SkillProgress> skillProgress = playerData.getSkillProperties();
                SkillProgress progress = skillProgress.getOrDefault(Skills.SEISMIC_IMPACT.getId(), new SkillProgress(1, 0.0));

                double radius = 1.0d * progress.getLevel();
                double damage = 1.5d * progress.getLevel();

                Location particleLocation = center.clone();
                particleLocation.setY(particleLocation.getY() + 0.1);

                double radiusEffect = Math.clamp(radius / 5, 1.0, 5.0);

                VisualEffectUtil.spawnParticleCircle(particleLocation, radiusEffect, 25, Sound.BLOCK_ANVIL_PLACE, Particle.CLOUD);

                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    if (entity instanceof LivingEntity victim && !entity.equals(player)) {
                        victim.damage(damage, player);
                        Vector pushDirection = victim.getLocation().toVector()
                                .subtract(center.toVector());

                        if (pushDirection.lengthSquared() == 0) {
                            pushDirection = new Vector(Math.random() - 0.5, 0, Math.random() - 0.5);
                        }

                        pushDirection.setY(0).normalize();
                        pushDirection.multiply(1.2);
                        pushDirection.setY(0.5);
                        victim.setVelocity(pushDirection);
                    }
                }
            }
        }
    }

    @Override
    public void onCommandExecute(Player player, PlayerData playerData, SkillProperties props) {
        player.sendMessage(Component.text("Seismic Impact", NamedTextColor.DARK_GRAY));
    }
}
