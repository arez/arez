package arez.persist.runtime.browser;

import arez.SafeProcedure;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.jspecify.annotations.Nullable;

/**
 * A StorageService that stores state as a single json blob in either the local or session storage of a browser.
 */
final class WebStorageService implements StorageService {
    @JsFunction
    private interface BeforeUnloadEventListener {
        void handleEvent(Object event);
    }

    @JsFunction
    private interface IdleRequestCallback {
        void onInvoke(Object deadline);
    }

    @JsType(isNative = true, name = "Storage", namespace = JsPackage.GLOBAL)
    private static class Storage {
        @JsMethod
        native @Nullable String getItem(String key);

        @JsMethod
        native void setItem(String key, String value);

        @JsMethod
        native void removeItem(String key);
    }

    @JsType(isNative = true, name = "JSON", namespace = JsPackage.GLOBAL)
    private static final class NativeJSON {
        @JsMethod
        private static native @Nullable Any parse(String value);

        @JsMethod
        private static native String stringify(Object value);
    }

    @JsType(isNative = true, name = "Object", namespace = JsPackage.GLOBAL)
    private static final class NativeObject {
        @JsMethod
        private static native String[] keys(Object value);
    }

    /**
     * A reference to the "beforeunload" listener so that the listener can be removed on disposed.
     */
    private final BeforeUnloadEventListener _beforeUnloadListener = e -> maybeCommit();
    /**
     * The browsers storage api targeted by the service.
     */
    private final Storage _storage;
    /**
     * The key used to store/access state in storage api.
     */
    private final String _address;
    /**
     * A cached copy of last trigger action supplied. Used to try and trigger save before app unloads.
     */
    @Nullable
    private SafeProcedure _commitTriggerAction;

    private int _idleCallbackId;

    static WebStorageService createSessionStorageService(final String persistenceKey) {
        return new WebStorageService(sessionStorage(), persistenceKey);
    }

    static WebStorageService createLocalStorageService(final String persistenceKey) {
        return new WebStorageService(localStorage(), persistenceKey);
    }

    private WebStorageService(final Storage storage, final String address) {
        _storage = storage;
        _address = address;
        // It should be noted that we don't
        addEventListener("beforeunload", _beforeUnloadListener);
    }

    @Override
    public void dispose() {
        if (0 != _idleCallbackId) {
            cancelIdleCallback(_idleCallbackId);
            _idleCallbackId = 0;
        }
        removeEventListener("beforeunload", _beforeUnloadListener);
    }

    @Override
    public void scheduleCommit(final SafeProcedure commitTriggerAction) {
        _commitTriggerAction = commitTriggerAction;
        // An alternative strategy is to send a message to a WebWorker containing the
        // state to save and performing the save in the other thread but we have yet
        // to see a scenario where performance requirements would warrant the extra complexity
        _idleCallbackId = requestIdleCallback(t -> commitTriggerAction.call());
    }

    @Override
    public void commit(final Map<Scope, Map<String, Map<String, Entry>>> state) {
        _idleCallbackId = 0;
        final var data = Js.<JsPropertyMap<@Nullable Object>>uncheckedCast(JsPropertyMap.of());
        for (final Map.Entry<Scope, Map<String, Map<String, Entry>>> scopeEntry : state.entrySet()) {
            final var scope = Js.<JsPropertyMap<@Nullable Object>>uncheckedCast(JsPropertyMap.of());
            final Set<Map.Entry<String, Map<String, Entry>>> entries =
                    scopeEntry.getValue().entrySet();
            if (!entries.isEmpty()) {
                for (final Map.Entry<String, Map<String, Entry>> entry : entries) {
                    final var type = Js.<JsPropertyMap<@Nullable Object>>uncheckedCast(JsPropertyMap.of());
                    for (final Map.Entry<String, Entry> instance :
                            entry.getValue().entrySet()) {
                        type.set(instance.getKey(), instance.getValue().getEncoded());
                    }
                    scope.set(entry.getKey(), type);
                }
                data.set(scopeEntry.getKey().getQualifiedName(), scope);
            }
        }
        if (0 == NativeObject.keys(data).length) {
            _storage.removeItem(_address);
        } else {
            _storage.setItem(_address, NativeJSON.stringify(data));
        }
    }

    @Override
    public Object encodeState(final Map<String, @Nullable Object> state, final TypeConverter converter) {
        final var encoded = Js.<JsPropertyMap<@Nullable Object>>uncheckedCast(JsPropertyMap.of());
        for (final Map.Entry<String, @Nullable Object> entry : state.entrySet()) {
            final String key = entry.getKey();
            encoded.set(key, converter.encode(key, entry.getValue()));
        }
        return encoded;
    }

    @Override
    public Map<String, @Nullable Object> decodeState(final Object encoded, final TypeConverter converter) {
        final JsPropertyMap<@Nullable Object> propertyMap = Js.cast(encoded);
        final Map<String, @Nullable Object> data = new HashMap<>();
        for (final String key : NativeObject.keys(propertyMap)) {
            data.put(key, converter.decode(key, propertyMap.get(key)));
        }
        return data;
    }

    @Override
    public void restore(final Map<Scope, Map<String, Map<String, Entry>>> state) {
        final String item = _storage.getItem(_address);
        if (null != item) {
            final Any value = NativeJSON.parse(item);
            assert null != value;
            final JsPropertyMap<@Nullable Object> scopes = value.asPropertyMap();
            for (final String scopeName : NativeObject.keys(scopes)) {
                restoreScope(
                        state,
                        scopeName,
                        Objects.requireNonNull(scopes.getAsAny(scopeName)).asPropertyMap());
            }
        }
    }

    private void restoreScope(
            final Map<Scope, Map<String, Map<String, Entry>>> state,
            final String scopeName,
            final JsPropertyMap<@Nullable Object> types) {
        for (final String typeName : NativeObject.keys(types)) {
            restoreType(
                    state,
                    scopeName,
                    typeName,
                    Objects.requireNonNull(types.getAsAny(typeName)).asPropertyMap());
        }
    }

    private void restoreType(
            final Map<Scope, Map<String, Map<String, Entry>>> state,
            final String scopeName,
            final String typeName,
            final JsPropertyMap<@Nullable Object> idMap) {
        final Scope scope = ArezPersist.findOrCreateScope(scopeName);
        final Map<String, Entry> entryMap = new HashMap<>();
        for (final String id : NativeObject.keys(idMap)) {
            final JsPropertyMap<@Nullable Object> encoded =
                    Objects.requireNonNull(idMap.getAsAny(id)).asPropertyMap();
            entryMap.put(id, new StorageService.Entry(null, encoded));
        }
        state.computeIfAbsent(scope, s -> new HashMap<>()).put(typeName, entryMap);
    }

    /**
     * If we have been supplied an action before, try to trigger a commit in case changes are in progress.
     */
    private void maybeCommit() {
        if (null != _commitTriggerAction) {
            _commitTriggerAction.call();
        }
    }

    @JsProperty(name = "window.sessionStorage", namespace = JsPackage.GLOBAL)
    private static native Storage sessionStorage();

    @JsProperty(name = "window.localStorage", namespace = JsPackage.GLOBAL)
    private static native Storage localStorage();

    @JsMethod(name = "window.addEventListener", namespace = JsPackage.GLOBAL)
    private static native void addEventListener(String type, BeforeUnloadEventListener listener);

    @JsMethod(name = "window.removeEventListener", namespace = JsPackage.GLOBAL)
    private static native void removeEventListener(String type, BeforeUnloadEventListener listener);

    @JsMethod(name = "window.requestIdleCallback", namespace = JsPackage.GLOBAL)
    private static native int requestIdleCallback(IdleRequestCallback callback);

    @JsMethod(name = "window.cancelIdleCallback", namespace = JsPackage.GLOBAL)
    private static native void cancelIdleCallback(int callbackId);
}
