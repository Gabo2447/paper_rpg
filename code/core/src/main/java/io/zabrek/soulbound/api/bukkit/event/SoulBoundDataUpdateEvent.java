package io.zabrek.soulbound.api.bukkit.event;

import io.zabrek.soulbound.api.profile.Profile;
import io.zabrek.soulbound.api.triggers.Trigger;
import org.bukkit.event.HandlerList;

public class SoulBoundDataUpdateEvent extends ProfileEvent {

    /**
     * A list of all handlers for this event.
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * Trigger id.
     */
    private final Class<? extends Trigger> tgrID;

    /**
     * Trigger data string.
     */
    private final String data;

    public SoulBoundDataUpdateEvent(final Profile profile, final Class<? extends Trigger> tgrID, final String data) {
        super(profile);
        this.tgrID = tgrID;
        this.data = data;
    }

    /**
     * Get the HandlerList of this event.
     *
     * @return the HandlerList
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Get the trigger id.
     *
     * @return the id of the trigger that changed
     */
    public Class<? extends Trigger> getTgrID() {
        return tgrID;
    }

    /**
     * Get the updated data.
     *
     * @return the new data string
     */
    public String getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
