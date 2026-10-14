package arez.persist.runtime;

import arez.SafeProcedure;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A StorageService that stores no state.
 */
final class NoopStorageService implements StorageService {
    @Override
    public void restore(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state) {}

    @Override
    public void dispose() {}

    @Override
    public void scheduleCommit(@NonNull final SafeProcedure commitTriggerAction) {
        commitTriggerAction.call();
    }

    @Override
    public void commit(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state) {
        // no-op
    }

    @NonNull
    @Override
    public Object encodeState(
            @NonNull final Map<String, @Nullable Object> state, @NonNull final TypeConverter converter) {
        // No serialization required as never stored to backend
        return state;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Map<String, @Nullable Object> decodeState(
            @NonNull final Object encoded, @NonNull final TypeConverter converter) {
        // No serialization required as never stored to backend
        return (Map<String, @Nullable Object>) encoded;
    }
}
