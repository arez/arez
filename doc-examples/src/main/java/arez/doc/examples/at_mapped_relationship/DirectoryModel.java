package arez.doc.examples.at_mapped_relationship;

import arez.annotations.ArezComponent;
import arez.annotations.MappedRelationship;
import arez.annotations.Observable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class DirectoryModel {
    @Observable
    public abstract List<Person> getPeople();

    public abstract void setPeople(List<Person> people);

    @MappedRelationship
    public abstract List<PersonRow> getRows();

    Stream<Person> rowsStream() {
        return getPeople().stream();
    }

    PersonRow createPersonRow(final Person person) {
        return PersonRow.create(person);
    }

    boolean filterPersonRow(final PersonRow row) {
        return !row.isHidden();
    }

    Comparator<PersonRow> rowsComparator() {
        return Comparator.comparing(PersonRow::getDisplayName);
    }

    boolean retainPersonRow(final PersonRow row) {
        return row.isPinned();
    }

    public static DirectoryModel create(final List<Person> people) {
        return new Arez_DirectoryModel(people);
    }
}
