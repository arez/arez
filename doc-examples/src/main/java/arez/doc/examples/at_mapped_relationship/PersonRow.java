package arez.doc.examples.at_mapped_relationship;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent(disposeOnDeactivate = true)
public abstract class PersonRow {
    private final String _lastKnownName;

    PersonRow(final String lastKnownName) {
        _lastKnownName = lastKnownName;
    }

    public static PersonRow create(final Person person) {
        return new Arez_PersonRow(person.getName(), person);
    }

    @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
    @Observable(initializer = Feature.ENABLE)
    public abstract @Nullable Person getPerson();

    public abstract void setPerson(@Nullable Person person);

    public String getDisplayName() {
        final Person person = getPerson();
        return null == person ? _lastKnownName : person.getName();
    }

    @Observable
    public abstract boolean isHidden();

    public abstract void setHidden(boolean hidden);

    @Observable
    public abstract boolean isPinned();

    public abstract void setPinned(boolean pinned);
}
