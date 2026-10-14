package arez.persist.runtime;

import static org.realityforge.braincheck.Guards.*;

import arez.SafeProcedure;
import arez.persist.StoreTypes;
import grim.annotations.OmitClinit;
import grim.annotations.OmitSymbol;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Provide an interface to register and access stores and access the root scope as well as global configuration settings.
 */
@OmitClinit
public final class ArezPersist {
    private ArezPersist() {}

    /**
     * Return true if apiInvariants will be checked.
     *
     * @return true if apiInvariants will be checked.
     */
    @OmitSymbol
    public static boolean shouldCheckApiInvariants() {
        return ArezPersistConfig.shouldCheckApiInvariants();
    }

    /**
     * Return true if the in-memory application store should be registered by the framework.
     *
     * @return true if the in-memory application store should be registered by the framework.
     */
    @OmitSymbol
    public static boolean isApplicationStoreEnabled() {
        return ArezPersistConfig.isApplicationStoreEnabled();
    }

    /**
     * Return the root scope under which all other scopes are nested.
     *
     * @return the root scope under which all other scopes are nested.
     */
    @NonNull
    public static Scope getRootScope() {
        return Registry.getRootScope();
    }

    /**
     * Register a store with specified name and storage service.
     * It is an error to register multiple stores with the same name.
     *
     * <p>As part of the register operation, the store will attempt to restore state from the storage service.
     * If an error occurs during the restore, then the error will be logged and registration will complete.</p>
     *
     * @param name    the name of the store.
     * @param service the associated StorageService.
     * @return the action to invoke to deregister service.
     */
    @NonNull
    public static SafeProcedure registerStore(@NonNull final String name, @NonNull final StorageService service) {
        return Registry.registerStore(name, service);
    }

    /**
     * Return the store that is registered with the specified name.
     * It is an error to invoke this method without registering a store under this name.
     *
     * @param name the name of the store.
     * @return the store.
     */
    @NonNull
    public static Store getStore(@NonNull final String name) {
        return Registry.getStore(name);
    }

    /**
     * Find the scope with the specified name.
     * The name can actually consist of name components separated by a "." character. Each name component is
     * nested within the scope identified by the prior name component. i.e. The name "dashboard.finance.entry"
     * will look for the scope named "entry" nested in a scope named "finance" nested in a scope named "dashboard".
     *
     * @param qualifiedName the qualified scope name.
     * @return the scope if it exists.
     */
    @Nullable
    public static Scope findScope(@NonNull final String qualifiedName) {
        Scope scope = getRootScope();
        if (Scope.ROOT_SCOPE_NAME.equals(qualifiedName)) {
            return scope;
        }
        int start = 0;
        int end;
        while (-1 != (end = qualifiedName.indexOf('.', start))) {
            scope = scope.findScope(qualifiedName.substring(start, end));
            if (null == scope) {
                return null;
            } else {
                start = end + 1;
            }
        }
        return scope.findScope(qualifiedName.substring(start));
    }

    /**
     * Find the scope with the specified name and if it does not exist then create it.
     * The name can actually consist of name components separated by a "." character. Each name component is
     * nested within the scope identified by the prior name component. i.e. The name "dashboard.finance.entry"
     * will look for the scope named "entry" nested in a scope named "finance" nested in a scope named "dashboard".
     *
     * @param qualifiedName the qualified scope name.
     * @return the scope.
     */
    @NonNull
    public static Scope findOrCreateScope(@NonNull final String qualifiedName) {
        Scope scope = getRootScope();
        if (Scope.ROOT_SCOPE_NAME.equals(qualifiedName)) {
            return scope;
        }
        int start = 0;
        int end;
        while (-1 != (end = qualifiedName.indexOf('.', start))) {
            scope = scope.findOrCreateScope(qualifiedName.substring(start, end));
            start = end + 1;
        }
        return scope.findOrCreateScope(qualifiedName.substring(start));
    }

    /**
     * Dispose the specified scope.
     * A dispose operation first performs a {@link #releaseScope(Scope)} on the scope, then attempts to
     * dispose all nested scopes and finally disposes the specified scope. A disposed scope should no longer be
     * used to store state. It is an error to attempt to dispose the root scope.
     *
     * @param scope the scope to dispose.
     */
    public static void disposeScope(@NonNull final Scope scope) {
        if (ArezPersist.shouldCheckApiInvariants()) {
            apiInvariant(
                    () -> !Scope.ROOT_SCOPE_NAME.equals(scope.getName()),
                    () -> "disposeScope() invoked with the root scope");
        }
        Registry.disposeScope(scope);
    }

    /**
     * Release the specified scope.
     * A release operation removes any state associated with the scope and any nested scope.
     *
     * @param scope the scope to release.
     */
    public static void releaseScope(@NonNull final Scope scope) {
        Registry.releaseScope(scope);
    }

    /**
     * Register a converter for a type.
     * It is an error to register multiple converters with the same name.
     *
     * @param type      the application type.
     * @param converter the converter.
     * @return the action to invoke to deregister converter.
     * @param <A> the type of the value.
     */
    @NonNull
    public static <A> SafeProcedure registerConverter(
            @NonNull final Class<A> type, @NonNull final Converter<A, ?> converter) {
        return Registry.registerConverter(type, converter);
    }

    /**
     * Return the converter registered for the specified application type or the identity converter if none are specified.
     *
     * @param type the application type.
     * @return the converter if any.
     * @param <A> the type of the value.
     */
    @NonNull
    public static <A> Converter<A, ?> getConverter(@NonNull final Class<A> type) {
        return Registry.getConverter(type);
    }

    static void registerApplicationStoreIfEnabled() {
        if (isApplicationStoreEnabled()) {
            registerStore(StoreTypes.APPLICATION, new NoopStorageService());
        }
    }
}
