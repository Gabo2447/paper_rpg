package com.zabrek.rpgplugin.infraestructure.minecraft.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VisualEffectUtil {
    public static void spawnParticleCircle(Location center, double radius, int totalParticles, Sound sound, Particle particle) {
        if (center.getWorld() != null) {
            float randomPitch = 0.8f + (new java.util.Random().nextFloat() * 0.4f);
            center.getWorld().playSound(center, sound, 1.0f, randomPitch);
        }

        double increment = (2 * Math.PI) / totalParticles;

        for (int i = 0; i < totalParticles; i++) {
            double angle = i * increment;

            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            double y = center.getY() + 0.1;

            Location particleLoc = new Location(center.getWorld(), x, y, z);
            center.getWorld().spawnParticle(particle, particleLoc, 1, 0, 0, 0, 0);
        }
    }

    /**
     * Plays an interface sound directly to the player (only the player will hear it)
     *
     * @param player The player who is interacting with the GUI
     * @param sound  The sound you want to play
     * @param volume The volume (1.0f is the standard)
     * @param pitch  Pitch/speed (1.0f is normal, >1.0 is high-pitched, <1.0 is low-pitched)
     */
    public static void playSoundPlayer(Player player, Sound sound, float volume, float pitch) {
        if (player != null && player.isOnline()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSoundPlayer(Player player, Sound sound) {
        playSoundPlayer(player, sound, 1.0f, 1.0f);
    }
}
