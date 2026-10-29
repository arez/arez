package arez.doc.examples.at_mapped_relationship;

import arez.annotations.ArezComponent;
import arez.annotations.MappedRelationship;
import arez.annotations.MappedRelationshipFactory;
import arez.annotations.Observable;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class DualDirectory {
    @Observable
    public abstract List<Person> getPeople();

    public abstract void setPeople(List<Person> people);

    @MappedRelationship
    public abstract List<PersonRow> getCompactRows();

    @MappedRelationship
    public abstract List<PersonRow> getDetailedRows();

    Stream<Person> compactRowsStream() {
        return getPeople().stream();
    }

    Stream<Person> detailedRowsStream() {
        return compactRowsStream();
    }

    @MappedRelationshipFactory(name = "compactRows")
    PersonRow buildCompact(final Person person) {
        return buildDetailed(person);
    }

    @MappedRelationshipFactory(name = "detailedRows")
    PersonRow buildDetailed(final Person person) {
        return PersonRow.create(person);
    }
}
