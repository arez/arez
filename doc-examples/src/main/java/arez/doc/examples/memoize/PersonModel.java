package arez.doc.examples.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class PersonModel {
    private @Nullable String _name;

    public static PersonModel create(final String name) {
        return new Arez_PersonModel(name);
    }

    PersonModel(final String name) {
        _name = name;
    }

    @Observable
    public @Nullable String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    @Memoize
    public boolean doesSearchMatch(final String value) {
        return getName().contains(value);
    }
}
