package arez.doc.examples.observables;

import arez.ObservableValue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ObservableExample {
    private @Nullable ObservableValue<Integer> _valueObservableValue;
    private int _value;

    public int getValue() {
        _valueObservableValue.reportObserved();
        return _value;
    }

    public void setValue(final int value) {
        _valueObservableValue.preReportChanged();
        if (value != _value) {
            _value = value;
            _valueObservableValue.reportChanged();
        }
    }
}
