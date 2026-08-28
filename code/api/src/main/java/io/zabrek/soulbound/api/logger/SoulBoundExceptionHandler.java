package io.zabrek.soulbound.api.logger;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundRunnable;
import io.zabrek.soulbound.api.common.function.SoulBoundSupplier;

/**
 * Can handle thrown {@link SoulBoundException} and rate limits them.
 * Useful in cases where errors might be thrown periodically.
 *
 * @since 2.0.0
 */
public interface SoulBoundExceptionHandler {

    /**
     * Runs a task and logs occurring quest exceptions with a rate limit.
     *
     * @param qeThrowing   a task that may throw a quest exception
     * @param defaultValue the default value to return in case of an exception
     * @param <T>          the type of the result
     * @return the result of the task or the default value if an exception occurs
     * @since 2.0.0
     */
    <T> T handle(SoulBoundSupplier<T> qeThrowing, T defaultValue);

    /**
     * Runs a task and logs occurring quest exceptions with a rate limit.
     *
     * @param qeThrowing a task that may throw a quest exception
     * @since 2.0.0
     */
    void handle(SoulBoundRunnable qeThrowing);
}