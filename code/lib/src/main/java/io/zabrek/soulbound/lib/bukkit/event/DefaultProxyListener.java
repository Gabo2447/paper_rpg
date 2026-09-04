package io.zabrek.soulbound.lib.bukkit.event;

import io.zabrek.soulbound.api.SoulBoundException;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.VisibleForTesting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The default implementation of {@link ProxyListener}.
 *
 * @param <T> The event type.
 */
public class DefaultProxyListener<T extends Event> implements ProxyListener<T> {

    /**
     * The handler list of the event.
     */
    @VisibleForTesting
    final HandlerList handlerList;

    /**
     * The event class type.
     */
    private final Class<T> eventClass;

    /**
     * The listener to catch the bukkit event.
     */
    private final RegisteredListener registeredListener;

    /**
     * To check if the listener is already registered since HandlerList does not offer this functionality.
     */
    private boolean registered;

    /**
     * Create a new proxy listener.
     *
     * @param eventClass The event class.
     * @param listener   The listener to catch the Bukkit event.
     * @throws SoulBoundException if extracting the handler list from the Bukkit event fails.
     */
    public DefaultProxyListener(final Class<T> eventClass, final RegisteredListener listener) throws SoulBoundException {
        this.eventClass = eventClass;
        this.registeredListener = listener;
        this.handlerList = getHandlerList();
        this.registered = false;
    }

    @Override
    public Class<T> getEventType() {
        return this.eventClass;
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }

    @Override
    public void register() {
        if (this.registered) {
            return;
        }
        this.handlerList.register(registeredListener);
        this.handlerList.bake();
        this.registered = true;
    }

    @Override
    public void unregister() {
        if (!this.registered) {
            return;
        }
        this.handlerList.unregister(registeredListener);
        this.handlerList.bake();
        this.registered = false;
    }

    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    private HandlerList getHandlerList() throws SoulBoundException {
        final String handlerRetrievalMethod = "getHandlerList";
        try {
            final Method getHandlerList = this.eventClass.getMethod(handlerRetrievalMethod);
            if (!getHandlerList.canAccess(null)) {
                getHandlerList.setAccessible(true);
            }
            return (HandlerList) getHandlerList.invoke(null);
        } catch (final NoSuchMethodException e) {
            throw new SoulBoundException("Could not find method `%s` of event `%s`. THIS IS FATAL."
                    .formatted(handlerRetrievalMethod, this.eventClass.getCanonicalName()), e);
        } catch (final InvocationTargetException e) {
            throw new SoulBoundException("Could not invoke method `%s` of event `%s`. THIS IS FATAL."
                    .formatted(handlerRetrievalMethod, this.eventClass.getCanonicalName()), e);
        } catch (final IllegalAccessException e) {
            throw new SoulBoundException("Could not access method `%s` of event `%s`. THIS IS FATAL."
                    .formatted(handlerRetrievalMethod, this.eventClass.getCanonicalName()), e);
        }
    }
}
