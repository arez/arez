package arez.integration.inverse;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ZeroOrOneMultiplicityExplicitLoadInverseIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var locatorLookupCallCount = new AtomicInteger();

        final var locator = new TypeBasedLocator();
        final var repository = PersonRepository.newRepository();

        Arez.context().registerLocator(locator);

        locator.registerLookup(Person.class, id -> {
            locatorLookupCallCount.incrementAndGet();
            return repository.findByArezId((Integer) id);
        });

        final Person person = repository.create();
        final Person person2 = repository.create();
        final Integer personId = Objects.requireNonNull(Identifiable.getArezId(person));
        final Integer person2Id = Objects.requireNonNull(Identifiable.getArezId(person2));

        assertEquals(locatorLookupCallCount.get(), 0);

        final var inverseCallCount = new AtomicInteger();
        observer(() -> {
            person.getCertificate();
            inverseCallCount.incrementAndGet();
        });

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);
        final var certificate1 = Certificate.create(personId);

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);

        safeAction(() -> Linkable.link(certificate1));

        assertEquals(locatorLookupCallCount.get(), 1);
        assertEquals(inverseCallCount.get(), 2);

        final var certificate1GetPersonCallCount = new AtomicInteger();
        final Observer certificate1GetPersonObserver = observer(() -> {
            certificate1.getPerson();
            certificate1GetPersonCallCount.incrementAndGet();
        });

        safeAction(() -> assertEquals(certificate1.getPerson(), person));
        safeAction(() -> assertEquals(person.getCertificate(), certificate1));
        safeAction(() -> assertNull(person2.getCertificate()));

        assertEquals(certificate1GetPersonCallCount.get(), 1);
        assertEquals(locatorLookupCallCount.get(), 1);

        safeAction(() -> {
            certificate1.setPersonId(person2Id);
            assertEquals(locatorLookupCallCount.get(), 1);
            Linkable.link(certificate1);
        });

        safeAction(() -> assertEquals(certificate1.getPerson(), person2));
        safeAction(() -> assertNull(person.getCertificate()));
        safeAction(() -> assertEquals(person2.getCertificate(), certificate1));

        assertEquals(inverseCallCount.get(), 3);
        assertEquals(certificate1GetPersonCallCount.get(), 2);
        assertEquals(locatorLookupCallCount.get(), 2);

        // Stop observer so we don't get an exception
        Disposable.dispose(certificate1GetPersonObserver);

        Disposable.dispose(certificate1);

        assertTrue(Disposable.isDisposed(certificate1));
        safeAction(() -> assertNull(person.getCertificate()));
        safeAction(() -> assertNull(person2.getCertificate()));

        assertEquals(certificate1GetPersonCallCount.get(), 2);
        assertEquals(locatorLookupCallCount.get(), 2);
    }

    @NullMarked
    @ArezComponent
    abstract static class Certificate {

        static Certificate create(final int personId) {
            return new ZeroOrOneMultiplicityExplicitLoadInverseIntegrationTest_Arez_Certificate(personId);
        }

        @Reference(inverseMultiplicity = Multiplicity.ZERO_OR_ONE, load = LinkType.EXPLICIT)
        abstract @Nullable Person getPerson();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        abstract int getPersonId();

        abstract void setPersonId(int personId);
    }

    @NullMarked
    @ArezComponent(observable = Feature.ENABLE)
    abstract static class Person {
        @Inverse
        @Nullable
        abstract Certificate getCertificate();
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class PersonRepository extends AbstractRepository<Integer, Person, PersonRepository> {
        static PersonRepository newRepository() {
            return new ZeroOrOneMultiplicityExplicitLoadInverseIntegrationTest_Arez_PersonRepository();
        }

        @Action
        Person create() {
            final var entity = new ZeroOrOneMultiplicityExplicitLoadInverseIntegrationTest_Arez_Person();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Person entity) {
            super.destroy(entity);
        }
    }
}
