package arez.persist.runtime;

import arez.SafeProcedure;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/**
 * A StorageService that stores no state.
 */
final class NoopStorageService implements StorageService {
    @Override
    public void restore(final Map<Scope, Map<String, Map<String, Entry>>> state) {}

    @Override
    public void dispose() {}

    @Override
    public void scheduleCommit(final SafeProcedure commitTriggerAction) {
        commitTriggerAction.call();
    }

    @Override
    public void commit(final Map<Scope, Map<String, Map<String, Entry>>> state) {
        // no-op
    }

    @Override
    public Object encodeState(final Map<String, @Nullable Object> state, final TypeConverter converter) {
        // No serialization required as never stored to backend
        return state;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, @Nullable Object> decodeState(final Object encoded, final TypeConverter converter) {
        // No serialization required as never stored to backend
        return (Map<String, @Nullable Object>) encoded;
    }
}
