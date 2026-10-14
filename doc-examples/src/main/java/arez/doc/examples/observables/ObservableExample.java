package arez.doc.examples.observables;

import arez.Arez;
import arez.ObservableValue;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ObservableExample {
    private final ObservableValue<Integer> _valueObservableValue =
            Arez.context().observable();
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
