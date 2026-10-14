package arez.doc.examples.at_observable_initial;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MyModel {
    @Observable
    abstract String getName();

    abstract void setName(String name);

    @Observable
    abstract int getAge();

    abstract void setAge(int age);

    @ObservableInitial
    static final String INITIAL_Name = "Anonymous";

    @ObservableInitial
    static int getInitialAge() {
        return 18;
    }
}
