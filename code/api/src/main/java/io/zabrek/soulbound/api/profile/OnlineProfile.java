package io.zabrek.soulbound.api.profile;

import org.bukkit.entity.Player;

/**
 * The OnlineProfile extends the {@link Profile} with the assumption that the profile's player is online.
 *
 * @since 2.0.0
 */
public interface OnlineProfile extends Profile {

    /**
     * Gets the player this profile belongs to.
     *
     * @return the {@link Player} of the profile
     * @since 2.0.0
     */
    @Override
    Player getPlayer();
}
