package io.zabrek.soulbound.utils;

import java.util.UUID;

/**
 * Utility class for managing feature flags and gradual rollouts.
 */
public final class FeatureFlagUtil {
    private FeatureFlagUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Checks if a feature is enabled for a specific target based on a rollout percentage.
     *
     * @param targetId    the unique identifier of the target
     * @param featureName the name of the feature
     * @param percentage  the rollout percentage (0 to 100)
     * @return true if the feature is enabled for the target, false otherwise
     */
    public static boolean isFeatureEnabled(final UUID targetId, final String featureName, final int percentage) {
        if (percentage < 0 || percentage > 100) {
            return false;
        }

        final String combinedKey = featureName + ":" + targetId.toString();
        final int hash = combinedKey.hashCode();
        final int bucket = Math.abs(hash) % 100;
        return bucket <= percentage;
    }
}
