package io.zabrek.soulbound.logger;

import io.zabrek.soulbound.api.logger.LogSource;
import org.apache.logging.log4j.core.internal.annotation.SuppressFBWarnings;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Custom {@link LogRecord} for SoulBound that adds a {@link Plugin} name and {@link LogSource} name.
 */
public class SoulBoundLogRecord extends LogRecord {

    /**
     * The serial.
     */
    @Serial
    private static final long serialVersionUID = -7094531905051980356L;

    /**
     * The plugin that logged this message.
     */
    private final String plugin;

    /**
     * The fully qualified log source that this log record is
     * about or {@link LogSource#EMPTY} if the record is not specific to a source.
     */
    @SuppressFBWarnings("SE_BAD_FIELD")
    private final LogSource logSource;

    /**
     * Creates a custom {@link LogRecord} that comes from a {@link Plugin}
     * and is not specific to any {@link LogSource}.
     *
     * @param level   level of the LogRecord
     * @param message raw non-localized logging message (maybe null)
     * @param plugin  plugin that logged this LogRecord
     */
    public SoulBoundLogRecord(final Level level, @Nullable final String message, final Plugin plugin) {
        this(level, message, plugin, LogSource.EMPTY);
    }

    /**
     * Creates a custom {@link LogRecord} that comes from a {@link Plugin} and may be specific to a {@link LogSource}.
     *
     * @param level     level of the LogRecord
     * @param message   raw non-localized logging message (maybe null)
     * @param plugin    plugin that logged this LogRecord
     * @param logSource log source this LogRecord is about; or {@link LogSource#EMPTY} if it is not about any specific source
     */
    public SoulBoundLogRecord(final Level level, @Nullable final String message, final Plugin plugin, final LogSource logSource) {
        super(level, message);
        this.plugin = plugin.getName();
        this.logSource = logSource;
    }

    /**
     * Creates a custom {@link LogRecord} that comes from a {@link Plugin} and may be specific to a {@link LogSource}.
     *
     * @param level      level of the LogRecord
     * @param message    raw non-localized logging message (maybe null)
     * @param pluginName name of the plugin that logged this LogRecord
     * @param source     fully qualified log source path this LogRecord is about;
     *                   or null if it is not about any specific source
     */
    public SoulBoundLogRecord(final Level level, @Nullable final String message, final String pluginName, @Nullable final String source) {
        super(level, message);
        this.plugin = pluginName;
        this.logSource = source == null ? LogSource.EMPTY : () -> source;
    }

    /**
     * Try to cast a {@link LogRecord} into a {@link SoulBoundLogRecord} and return an {@link Optional} that contains
     * the cast record if successful.
     *
     * @param record record that may be a {@link SoulBoundLogRecord}
     * @return optional containing the same record as {@link SoulBoundLogRecord} if possible
     */
    public static Optional<SoulBoundLogRecord> safeCast(final LogRecord record) {
        if (record instanceof SoulBoundLogRecord) {
            return Optional.of((SoulBoundLogRecord) record);
        }
        return Optional.empty();
    }

    /**
     * Gets the {@link LogSource} that this record is about.
     *
     * @return {@link LogSource} containing the fully qualified source path
     */
    public LogSource getLogSource() {
        return logSource;
    }

    /**
     * Gets the name of the plugin that logged this record.
     *
     * @return The plugin.
     */
    public String getPlugin() {
        return plugin;
    }
}