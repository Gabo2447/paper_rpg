package io.zabrek.soulbound.api;

import java.io.Serial;

/**
 * This exception is thrown when a problem occurs in the plugin.
 */
public class SoulBoundException extends Exception {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 7487088647464022627L;

    /**
     * {@link Exception#Exception(String)}.
     *
     * @param message the exception message.
     */
    public SoulBoundException(final String message) {
        super(message);
    }

    /**
     * {@link Exception#Exception(String, Throwable)}.
     *
     * @param message the exception message.
     * @param cause   the Throwable that caused this exception.
     */
    public SoulBoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * {@link Exception#Exception(Throwable)}.
     *
     * @param cause the exceptions cause.
     */
    public SoulBoundException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        final String message = super.getMessage();
        if (message == null) {
            throw new IllegalStateException("Message is null");
        }
        return message;
    }
}
