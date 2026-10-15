package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import grim.annotations.OmitType;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A implementation of {@link ComponentInfo} that proxies to a {@link Component}.
 */
@OmitType(unless = "arez.enable_spies")
@OmitType(unless = "arez.enable_native_components")
final class ComponentInfoImpl implements ComponentInfo {
    private final Component _component;
    private final List<ObservableValue<?>> _observableValues;
    private final List<ComputableValue<?>> _computableValues;

    ComponentInfoImpl(final Component component) {
        _component = component;
        _observableValues = Collections.unmodifiableList(_component.getObservableValues());
        _computableValues = Collections.unmodifiableList(_component.getComputableValues());
    }

    @Override
    public String getType() {
        return _component.getType();
    }

    @Override
    public Object getId() {
        return _component.getId();
    }

    @Override
    public String getName() {
        return _component.getName();
    }

    @Override
    public List<ObservableValueInfo> getObservableValues() {
        return ObservableValueInfoImpl.asUnmodifiableInfos(_observableValues);
    }

    @Override
    public List<ObserverInfo> getObservers() {
        return ObserverInfoImpl.asUnmodifiableInfos(_component.getObservers());
    }

    @Override
    public List<ComputableValueInfo> getComputableValues() {
        return ComputableValueInfoImpl.asUnmodifiableInfos(_computableValues);
    }

    @Override
    public boolean isDisposed() {
        return _component.isDisposed();
    }

    @Override
    public String toString() {
        return _component.toString();
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            final var that = (ComponentInfoImpl) o;
            return _component.equals(that._component);
        }
    }

    @Override
    public int hashCode() {
        return _component.hashCode();
    }
}
