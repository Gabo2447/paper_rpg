package io.zabrek.soulbound.lib.logger;

import io.zabrek.soulbound.api.SoulBoundException;
import io.zabrek.soulbound.api.common.function.SoulBoundRunnable;
import io.zabrek.soulbound.api.common.function.SoulBoundSupplier;
import io.zabrek.soulbound.api.logger.LogSource;
import io.zabrek.soulbound.api.logger.SoulBoundExceptionHandler;
import io.zabrek.soulbound.api.logger.SoulBoundLogger;

/**
 * Can handle thrown {@link SoulBoundException} and rate limits them so
 * they don't spam console that hard.
 */
public class DefaultSoulBoundExceptionHandler implements SoulBoundExceptionHandler {

    /**
     * The default minimal interval in which errors are logged.
     */
    public static final int DEFAULT_ERROR_RATE_LIMIT_MILLIS = 5000;

    /**
     * The minimal interval in which errors are logged.
     */
    private final int errorRateLimit;

    /**
     * The logger instance to use.
     */
    private final SoulBoundLogger logger;

    /**
     * The associated source for logging.
     */
    private final LogSource source;

    /**
     * All additional source details to log.
     */
    private final String sourceDetails;

    /**
     * The last {@link System#currentTimeMillis()} timestamp when an error message was logged.
     */
    private long last;

    /**
     * Creates a new {@link DefaultSoulBoundExceptionHandler} instance.
     *
     * @param source         the source to use for logging
     * @param logger         the logger to use
     * @param errorRateLimit the minimal interval in which errors are logged
     * @param sourceDetails  additional source details to log
     */
    public DefaultSoulBoundExceptionHandler(final LogSource source, final SoulBoundLogger logger,
                                            final int errorRateLimit, final String... sourceDetails) {
        this.logger = logger;
        this.source = source;
        this.errorRateLimit = errorRateLimit;
        this.sourceDetails = sourceDetails.length > 0 ? "{'" + String.join("', '", sourceDetails) + "'}" : "";
    }

    /**
     * Creates a new {@link DefaultSoulBoundExceptionHandler} instance with an error rate limit of DEFAULT_ERROR_RATE_LIMIT_MILLIS.
     *
     * @param source        the source to use for logging
     * @param logger        the logger to use
     * @param sourceDetails additional source details to log
     */
    public DefaultSoulBoundExceptionHandler(final LogSource source, final SoulBoundLogger logger, final String... sourceDetails) {
        this(source, logger, DEFAULT_ERROR_RATE_LIMIT_MILLIS, sourceDetails);
    }

    @Override
    public <T> T handle(final SoulBoundSupplier<T> qeThrowing, final T defaultValue) {
        try {
            return qeThrowing.get();
        } catch (final SoulBoundException e) {
            handleException(e);
            return defaultValue;
        }
    }

    @Override
    public void handle(final SoulBoundRunnable qeThrowing) {
        try {
            qeThrowing.run();
        } catch (final SoulBoundException e) {
            handleException(e);
        }
    }

    private void handleException(final SoulBoundException e) {
        if (System.currentTimeMillis() - last > errorRateLimit) {
            last = System.currentTimeMillis();
            logger.warn(source, "%sError while handling: ".formatted(sourceDetails) + e.getMessage(), e);
        }
    }
}
