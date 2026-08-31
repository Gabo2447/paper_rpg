package io.zabrek.soulbound.config.migrator;

import io.zabrek.soulbound.api.logger.SoulBoundLoggerFactory;
import io.zabrek.soulbound.lib.config.patcher.migration.Migration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles the migration process of general structure changes.
 */
public class Migrator {

    /**
     * The migrations to use.
     */
    private final List<Migration> migrations;

    /**
     * Creates a new generic migration process.
     *
     * @param loggerFactory the logger factory.
     */
    public Migrator(final SoulBoundLoggerFactory loggerFactory) {
        this.migrations = new LinkedList<>();
        // migrations.add(new ...());
    }

    /**
     * Migrates all generic configs or changes.
     *
     * @throws IOException if an errors occurs
     */
    public void migrate() throws IOException {
        for (final Migration migration : migrations) {
            migration.migrate();
        }
    }
}
