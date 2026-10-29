package arez.doc.examples.at_mapped_relationship;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class Person {
    @Observable
    public abstract String getName();

    public abstract void setName(String name);

    public static Person create(final String name) {
        return new Arez_Person(name);
    }
}
