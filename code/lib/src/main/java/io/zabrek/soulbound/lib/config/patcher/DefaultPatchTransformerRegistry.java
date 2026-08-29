package io.zabrek.soulbound.lib.config.patcher;

import io.zabrek.soulbound.api.config.patcher.PatchTransformer;
import io.zabrek.soulbound.api.config.patcher.PatchTransformerRegistry;
import io.zabrek.soulbound.lib.config.patcher.transformer.KeyRenameTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.ListEntryAddTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.ListEntryRemoveTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.ListEntryRenameTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.RemoveTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.SetTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.TypeTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.ValueRenameTransformer;
import io.zabrek.soulbound.lib.config.patcher.transformer.ValueReplaceTransformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link PatchTransformerRegistry} containing all transformers of SoulBound.
 */
public class DefaultPatchTransformerRegistry implements PatchTransformerRegistry {

    /**
     * A map of transformers to use for patching and their names.
     */
    protected final Map<String, PatchTransformer> transformers;

    /**
     * Creates a new {@link DefaultPatchTransformerRegistry}.
     */
    public DefaultPatchTransformerRegistry() {
        transformers = new HashMap<>();
        transformers.put("SET", new SetTransformer());
        transformers.put("REMOVE", new RemoveTransformer());
        transformers.put("KEY_RENAME", new KeyRenameTransformer());
        transformers.put("VALUE_RENAME", new ValueRenameTransformer());
        transformers.put("VALUE_REPLACE", new ValueReplaceTransformer());
        transformers.put("LIST_ENTRY_ADD", new ListEntryAddTransformer());
        transformers.put("LIST_ENTRY_REMOVE", new ListEntryRemoveTransformer());
        transformers.put("LIST_ENTRY_RENAME", new ListEntryRenameTransformer());
        transformers.put("TYPE_TRANSFORM", new TypeTransformer());
    }

    @Override
    public Map<String, PatchTransformer> getTransformers() {
        return transformers;
    }
}
