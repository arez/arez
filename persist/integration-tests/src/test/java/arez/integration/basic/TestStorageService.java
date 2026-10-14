package arez.integration.basic;

import arez.SafeProcedure;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class TestStorageService implements StorageService {
    @NonNull
    private final Map<Scope, Map<String, Map<String, Entry>>> _initialState;

    @NonNull
    private final Map<Scope, Map<String, Map<String, Entry>>> _state = new HashMap<>();

    public TestStorageService(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> initialState) {
        _initialState = Objects.requireNonNull(initialState);
    }

    @NonNull
    public Map<Scope, Map<String, Map<String, Entry>>> getState() {
        return _state;
    }

    @Override
    public void dispose() {}

    @Override
    public void scheduleCommit(@NonNull final SafeProcedure commitTriggerAction) {
        commitTriggerAction.call();
    }

    @Override
    public void commit(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state) {
        _state.clear();
        _state.putAll(state);
    }

    @Override
    public void restore(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state) {
        state.clear();
        state.putAll(_initialState);
    }

    @NonNull
    @Override
    public Object encodeState(
            @NonNull final Map<String, @Nullable Object> state, @NonNull final TypeConverter converter) {
        return state;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Map<String, @Nullable Object> decodeState(
            @NonNull final Object encoded, @NonNull final TypeConverter converter) {
        return (Map<String, @Nullable Object>) encoded;
    }
}
