package arez;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * A compact insertion-ordered map specialized for hook storage.
 */
final class HookMap {

    private @Nullable String[] _keys = new String[1];

    private @Nullable Hook[] _values = new Hook[1];

    private int _size;

    int size() {
        return _size;
    }

    boolean isEmpty() {
        return 0 == _size;
    }

    void clear() {
        for (int i = 0; i < _size; i++) {
            _keys[i] = null;
            _values[i] = null;
        }
        _size = 0;
    }

    boolean containsKey(final String key) {
        return -1 != findKeyIndex(key);
    }

    @Nullable
    Hook get(final String key) {
        final int index = findKeyIndex(key);
        return -1 == index ? null : _values[index];
    }

    void put(final String key, final Hook value) {
        final int index = findKeyIndex(key);
        if (-1 != index) {
            _values[index] = value;
        } else {
            ensureCapacity();
            _keys[_size] = key;
            _values[_size] = value;
            _size++;
        }
    }

    String keyAt(final int index) {
        return Objects.requireNonNull(_keys[index]);
    }

    Hook valueAt(final int index) {
        return Objects.requireNonNull(_values[index]);
    }

    private void ensureCapacity() {
        if (_size == _keys.length) {
            final int length = (_size * 2) + 1;
            final @Nullable String[] keys = new String[length];
            final @Nullable Hook[] values = new Hook[length];
            for (int i = 0; i < _size; i++) {
                keys[i] = _keys[i];
                values[i] = _values[i];
            }
            _keys = keys;
            _values = values;
        }
    }

    private int findKeyIndex(final String key) {
        for (int i = 0; i < _size; i++) {
            if (key.equals(_keys[i])) {
                return i;
            }
        }
        return -1;
    }
}
