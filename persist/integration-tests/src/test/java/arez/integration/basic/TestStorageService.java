package arez.integration.basic;

import static org.testng.Assert.assertNotNull;

import arez.SafeProcedure;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public final class TestStorageService implements StorageService {
    private final Map<Scope, Map<String, Map<String, Entry>>> _initialState;

    private final Map<Scope, Map<String, Map<String, Entry>>> _state = new HashMap<>();

    public TestStorageService(final Map<Scope, Map<String, Map<String, Entry>>> initialState) {
        assertNotNull(initialState);
        _initialState = initialState;
    }

    public Map<Scope, Map<String, Map<String, Entry>>> getState() {
        return _state;
    }

    @Override
    public void dispose() {}

    @Override
    public void scheduleCommit(final SafeProcedure commitTriggerAction) {
        commitTriggerAction.call();
    }

    @Override
    public void commit(final Map<Scope, Map<String, Map<String, Entry>>> state) {
        _state.clear();
        _state.putAll(state);
    }

    @Override
    public void restore(final Map<Scope, Map<String, Map<String, Entry>>> state) {
        state.clear();
        state.putAll(_initialState);
    }

    @Override
    public Object encodeState(final Map<String, @Nullable Object> state, final TypeConverter converter) {
        return state;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, @Nullable Object> decodeState(final Object encoded, final TypeConverter converter) {
        return (Map<String, @Nullable Object>) encoded;
    }
}
