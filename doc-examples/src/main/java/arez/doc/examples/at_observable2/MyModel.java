package arez.doc.examples.at_observable2;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class MyModel {
    @Observable
    public abstract int getValue();

    public abstract void setValue(int value);
}
