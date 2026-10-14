package arez.integration;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class EqualsAndHashCodeTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class PersonModel {

        private String _firstName;

        static PersonModel create(final String firstName) {
            return new EqualsAndHashCodeTest_Arez_PersonModel(firstName);
        }

        PersonModel(final String firstName) {
            _firstName = firstName;
        }

        @Observable
        String getFirstName() {
            return _firstName;
        }

        void setFirstName(final String firstName) {
            _firstName = firstName;
        }
    }

    @Test
    public void equalsAndHashBasedOnId() {
        final var person1 = PersonModel.create(ValueUtil.randomString());
        final var person2 = PersonModel.create(ValueUtil.randomString());
        final PersonModel person3 = safeAction(() -> PersonModel.create(person1.getFirstName()));

        assertEquals(person1.hashCode(), person1.hashCode());
        assertNotEquals(person1.hashCode(), person2.hashCode());
        assertNotEquals(person1.hashCode(), person3.hashCode());

        assertEquals(person1, person1);
        assertNotEquals(person1, person2);
        assertNotEquals(person1, person3);
    }
}
