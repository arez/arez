package arez.integration;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Zone;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MultiZoneIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void multiZoneScenario() throws Throwable {
        ArezTestUtil.enableZones();

        final Zone zone1 = Arez.createZone();
        final Zone zone2 = Arez.createZone();

        final ArezContext context1 = zone1.getContext();
        final ArezContext context2 = zone2.getContext();

        final var recorder = new SpyEventRecorder();
        context1.getSpy().addSpyEventHandler(recorder);
        context2.getSpy().addSpyEventHandler(recorder);

        final AtomicReference<@Nullable PersonModel> person = new AtomicReference<>();
        final AtomicReference<@Nullable PersonModel> person2 = new AtomicReference<>();
        zone1.run(() -> {
            person.set(PersonModel.create("Bill", "Smith"));
            zone2.run(() -> person2.set(PersonModel.create("Bill", "Smith")));
        });

        context1.observer("FirstNamePrinter1", () -> {
            observeADependency();
            recorder.mark("firstName1", Objects.requireNonNull(person.get()).getFirstName());
        });
        context2.observer("FirstNamePrinter2", () -> {
            observeADependency();
            recorder.mark("firstName2", Objects.requireNonNull(person2.get()).getFirstName());
        });

        context1.observer("FullNamePrinter1", () -> {
            observeADependency();
            recorder.mark("fullname1", Objects.requireNonNull(person.get()).getFullName());
        });
        context2.observer("FullNamePrinter2", () -> {
            observeADependency();
            recorder.mark("fullname2", Objects.requireNonNull(person2.get()).getFullName());
        });

        context1.action(
                "First Name Update1", () -> Objects.requireNonNull(person.get()).setFirstName("Fred"));
        context1.action(
                "Last Name Update1", () -> Objects.requireNonNull(person.get()).setLastName("Donaldo"));

        context2.action(
                "Last Name Update2", () -> Objects.requireNonNull(person2.get()).setLastName("Donaldo"));

        assertMatchesFixture(recorder);
    }

    @Test
    public void multiZoneScenario_transactionAlignment() throws Throwable {
        ArezTestUtil.enableZones();

        final Zone zone1 = Arez.createZone();
        final Zone zone2 = Arez.createZone();

        final ArezContext context1 = zone1.getContext();
        final ArezContext context2 = zone2.getContext();

        final AtomicReference<@Nullable PersonModel> person1 = new AtomicReference<>();
        final AtomicReference<@Nullable PersonModel> person2 = new AtomicReference<>();

        zone1.run(() -> {
            person1.set(PersonModel.create("Bill", "Smith"));
            zone2.run(() -> person2.set(PersonModel.create("Bill", "Smith")));
        });

        context1.action(() -> assertInTransaction(Objects.requireNonNull(person1.get())));
        assertNotInTransaction(Objects.requireNonNull(person2.get()));
        assertNotInTransaction(Objects.requireNonNull(person1.get()));
        context2.action(() -> assertInTransaction(Objects.requireNonNull(person2.get())));
    }

    /**
     * Test we are in a transaction by trying to observe an observable.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void assertInTransaction(final PersonModel person) {
        person.getFirstName();
    }

    /**
     * Test we are not in a transaction by trying to observe an observable.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void assertNotInTransaction(final PersonModel person) {
        assertThrows(person::getFirstName);
    }

    @NullMarked
    @ArezComponent
    abstract static class PersonModel {

        private String _firstName;

        private String _lastName;

        static PersonModel create(final String firstName, final String lastName) {
            return new MultiZoneIntegrationTest_Arez_PersonModel(firstName, lastName);
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
