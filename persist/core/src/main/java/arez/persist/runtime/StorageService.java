package arez.persist.runtime;

import arez.Arez;
import arez.SafeProcedure;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Interface implemented to provide the state storage and retrieval services.
 */
public interface StorageService {
    /**
     * Dispose the service.
     * The service may release any resources no longer required after dispose has been
     * called. It is expected that no other methods will be invoked after this method
     * has been invoked.
     */
    void dispose();

    /**
     * Method invoked when state has changed.
     * The storage service is expected to invoke the {@code commitTriggerAction.call()}
     * method when service wants to commit changes to the service. This is likely to be
     * invoked many times in quick succession and it is up to the storage service to decide
     * how to batch up the changes and thus when to schedule the commit.
     *
     * @param commitTriggerAction the action to invoke when a commit should be triggered.
     */
    void scheduleCommit(@NonNull SafeProcedure commitTriggerAction);

    /**
     * Invoked by the store to save the state to the storage service.
     * The map contains scopes, mapped to component types, mapped to component ids, map
     * to instance data for that component.
     *
     * @param state the state to store.
     */
    void commit(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state);

    /**
     * Restore state from the service into the specified state parameter.
     *
     * @param state the map in which to store data retrieved from the service.
     */
    void restore(@NonNull final Map<Scope, Map<String, Map<String, Entry>>> state);

    /**
     * Encode state in a way that makes storage to the backend easier.
     * This is invoked when the state change occurs.
     *
     * @param state     the component state.
     * @param converter the converter for the type.
     * @return the encoded form for this service.
     */
    @NonNull
    Object encodeState(@NonNull Map<String, @Nullable Object> state, @NonNull final TypeConverter converter);

    /**
     * Decode state from backend storage form.
     * This occurs on access to state.
     *
     * @param encoded   the encoded component state.
     * @param converter the converter for the type.
     * @return the decoded form of component state.
     */
    @NonNull
    Map<String, @Nullable Object> decodeState(@NonNull Object encoded, @NonNull final TypeConverter converter);

    /**
     * An entry containing the state for a particular component.
     */
    final class Entry {
        @Nullable
        private Map<String, @Nullable Object> _data;

        @NonNull
        private final Object _encoded;

        public Entry(@Nullable final Map<String, @Nullable Object> data, @NonNull final Object encoded) {
            _data = data;
            _encoded = encoded;
        }

        /**
         * Return the decoded data.
         *
         * @return the decoded data.
         */
        @Nullable
        public Map<String, @Nullable Object> getData() {
            return _data;
        }

        void setData(@Nullable final Map<String, @Nullable Object> data) {
            _data = data;
        }

        /**
         * Return the encoded data.
         *
         * @return the encoded data.
         */
        @NonNull
        public Object getEncoded() {
            return _encoded;
        }

        @Override
        public String toString() {
            if (Arez.areNamesEnabled()) {
                return String.valueOf(_data);
            } else {
                return super.toString();
            }
        }
    }
}
