package io.zabrek.soulbound.database;

import org.apache.logging.log4j.core.internal.annotation.SuppressFBWarnings;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Saves the data to the database asynchronously.
 */
@SuppressWarnings({"PMD.DoNotUseThreads", "PMD.AvoidSynchronizedStatement"})
@SuppressFBWarnings("IS2_INCONSISTENT_SYNC")
public class AsyncSaver implements Runnable, Saver {

    /**
     * Logger instance for this class.
     */
    private final Logger logger;

    /**
     * The connector that connects to the database.
     */
    private final Connector conn;

    /**
     * The queue of records to be saved to the database.
     */
    private final Queue<Record> queue;

    /**
     * Whether the saver currently running or not.
     */
    private final boolean running;

    public AsyncSaver(final Logger logger, final Connector connector) {
        this.logger = logger;
        this.conn = connector;
        this.queue = new ConcurrentLinkedQueue<>();
        this.running = true;
    }
}
