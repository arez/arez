package arez.integration.basic;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import arez.persist.StoreTypes;
import arez.persist.runtime.ArezPersist;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

@ArezComponent(observable = Feature.ENABLE, disposeNotifier = Feature.ENABLE)
@PersistType(defaultStore = StoreTypes.LOCAL)
public abstract class NullableValue {
    private final String _id;

    static NullableValue create(final String id) {
        final NullableValue value = new Arez_NullableValue(id);
        NullableValue_PersistSidecar.attach(ArezPersist.getRootScope(), value);
        return value;
    }

    NullableValue(final String id) {
        _id = Objects.requireNonNull(id);
    }

    @ComponentId
    String getId() {
        return _id;
    }

    @Persist
    @Observable
    public abstract @Nullable String getValue();

    public abstract void setValue(@Nullable String value);
}
