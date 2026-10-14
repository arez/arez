package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeKeepAliveTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final var person = PersonModel.create("Bill", "Smith");

        context.action("First Name Update", () -> person.setFirstName("Fred"));
        context.action("Last Name Update", () -> person.setLastName("Donaldo"));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @SuppressWarnings("WeakerAccess")
    @ArezComponent
    public abstract static class PersonModel {

        private String _firstName;

        private String _lastName;

        public static PersonModel create(final String firstName, final String lastName) {
            return new MemoizeKeepAliveTest_Arez_PersonModel(firstName, lastName);
        }

        PersonModel(final String firstName, final String lastName) {
            _firstName = firstName;
            _lastName = lastName;
        }

        @Observable
        public String getFirstName() {
            return _firstName;
        }

        public void setFirstName(final String firstName) {
            _firstName = firstName;
        }

        @Observable
        public String getLastName() {
            return _lastName;
        }

        public void setLastName(final String lastName) {
            _lastName = lastName;
        }

        @Memoize(keepAlive = true)
        public String getFullName() {
            return getFirstName() + " " + getLastName();
        }
    }
}
