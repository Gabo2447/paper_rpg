package io.zabrek.soulbound;

import java.util.Set;

/**
 * Represents Multi Module Entry Point for SoulBound plugin.
 */
public class SoulBoundPlugin extends SoulBound {

    /**
     * All of those classes have to exist to determine the server software to be Paper.
     */
    public static final Set<String> PAPER_IDENTIFYING_CLASSES =
            Set.of("com.destroystokyo.paper.PaperConfig", "io.papermc.paper.configuration.Configuration");

    /**
     * The required default constructor without arguments for plugin creation.
     */
    public SoulBoundPlugin() {
        super();
    }

    @Override
    public void onEnable() {
        if (!isPaper()) {
            getLogger().severe("SoulBound requires Paper to run!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        super.onEnable();
    }

    private boolean isPaper() {
        return PAPER_IDENTIFYING_CLASSES.stream().anyMatch(this::testClass);
    }

    private boolean testClass(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }
}