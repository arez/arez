package arez.integration.observable;

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
public final class ObservableInitializerIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final PersonModel person = PersonModel.create("Bill", "Smith");

        context.observer("FirstNamePrinter", () -> {
            observeADependency();
            recorder.mark("firstName", person.getFirstName());
        });
        context.observer("FullNamePrinter", () -> {
            observeADependency();
            recorder.mark("fullname", person.getFullName());
        });

        context.action("First Name Update", () -> person.setFirstName("Fred"));
        context.action("Last Name Update", () -> person.setLastName("Donaldo"));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @SuppressWarnings("SameParameterValue")
    @ArezComponent
    abstract static class PersonModel {

        static PersonModel create(final String firstName, final String lastName) {
            return new ObservableInitializerIntegrationTest_Arez_PersonModel(firstName, lastName);
        }

        @Observable
        abstract String getFirstName();

        abstract void setFirstName(String firstName);

        @Observable
        abstract String getLastName();

        abstract void setLastName(String lastName);

        @Memoize
        String getFullName() {
            return getFirstName() + " " + getLastName();
        }
    }
}
