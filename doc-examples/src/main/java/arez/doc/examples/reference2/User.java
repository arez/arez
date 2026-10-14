package arez.doc.examples.reference2;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class User {
    private final int _id;

    private String _name;

    private boolean _active;

    User(final int id, final String name) {
        _id = id;
        _name = Objects.requireNonNull(name);
    }

    @ComponentId
    public int getId() {
        return _id;
    }

    @Observable
    public String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(final boolean active) {
        _active = active;
    }
}
