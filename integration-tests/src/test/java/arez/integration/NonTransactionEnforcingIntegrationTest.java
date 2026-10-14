package arez.integration;

import static org.testng.Assert.*;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class NonTransactionEnforcingIntegrationTest extends AbstractArezIntegrationTest {
    /**
     * Tests integration between observed and actions and sequencing of operations.
     */
    @Test
    public void personScenario() throws Throwable {
        ArezTestUtil.noEnforceTransactionType();
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

        final AtomicBoolean action1ReadOnly = new AtomicBoolean(true);
        final AtomicBoolean action2ReadOnly = new AtomicBoolean(true);

        // Note that the next two actions pass in mutation false but actually perform mutation
        // This is to verify that transactions are really not set
        context.action(
                "First Name Update",
                () -> {
                    person.setFirstName("Fred");
                    action1ReadOnly.set(context.getSpy().getTransaction().isReadOnly());
                },
                ActionFlags.READ_ONLY);
        context.action(
                "Last Name Update",
                () -> {
                    person.setLastName("Donaldo");
                    action2ReadOnly.set(context.getSpy().getTransaction().isReadOnly());
                },
                ActionFlags.READ_ONLY);

        // When transactions are not enforced, everything is effectively a write transaction!!!
        assertFalse(action1ReadOnly.get());
        assertFalse(action2ReadOnly.get());

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent
    abstract static class PersonModel {

        private String _firstName;

        private String _lastName;

        static PersonModel create(final String firstName, final String lastName) {
            return new NonTransactionEnforcingIntegrationTest_Arez_PersonModel(firstName, lastName);
        }

        PersonModel(final String firstName, final String lastName) {
            _firstName = firstName;
            _lastName = lastName;
        }

        @Observable
        String getFirstName() {
            return _firstName;
        }

        void setFirstName(final String firstName) {
            _firstName = firstName;
        }

        @Observable
        String getLastName() {
            return _lastName;
        }

        void setLastName(final String lastName) {
            _lastName = lastName;
        }

        @Memoize
        String getFullName() {
            return getFirstName() + " " + getLastName();
        }
    }
}
