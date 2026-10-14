package arez.doc.examples.repository;

import arez.annotations.ArezComponentLike;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
public class MyComponent {
    private final int _id;

    private String _name;

    private boolean _active;

    MyComponent(final int id, final String name) {
        _id = id;
        _name = Objects.requireNonNull(name);
    }

    @ComponentId
    public final int getId() {
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
