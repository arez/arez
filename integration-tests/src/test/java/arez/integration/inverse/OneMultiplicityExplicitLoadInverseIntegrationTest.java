package arez.integration.inverse;

import static org.testng.Assert.*;

import arez.Arez;
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
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class OneMultiplicityExplicitLoadInverseIntegrationTest extends AbstractArezIntegrationTest {
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
        final Integer personId = Identifiable.getArezId(person);
        assertNotNull(personId);
        assert null != personId;
        final Integer person2Id = Identifiable.getArezId(person2);
        assertNotNull(person2Id);
        assert null != person2Id;

        assertEquals(locatorLookupCallCount.get(), 0);

        final var inverseCallCount = new AtomicInteger();
        observer(() -> {
            person.getCertificate();
            inverseCallCount.incrementAndGet();
        });

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);
        final var certificate1 = Certificate.create(personId);
        final var certificate2 = Certificate.create(person2Id);

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);

        safeAction(() -> Linkable.link(certificate1));
        safeAction(() -> Linkable.link(certificate2));

        assertEquals(locatorLookupCallCount.get(), 2);
        assertEquals(inverseCallCount.get(), 2);

        final var certificate1GetPersonCallCount = new AtomicInteger();
        observer(() -> {
            certificate1.getPerson();
            certificate1GetPersonCallCount.incrementAndGet();
        });

        final var certificate2GetPersonCallCount = new AtomicInteger();
        observer(() -> {
            certificate2.getPerson();
            certificate2GetPersonCallCount.incrementAndGet();
        });

        safeAction(() -> assertEquals(certificate1.getPerson(), person));
        safeAction(() -> assertEquals(certificate2.getPerson(), person2));
        safeAction(() -> assertEquals(person.getCertificate(), certificate1));
        safeAction(() -> assertEquals(person2.getCertificate(), certificate2));

        assertEquals(certificate1GetPersonCallCount.get(), 1);
        assertEquals(certificate2GetPersonCallCount.get(), 1);
        assertEquals(locatorLookupCallCount.get(), 2);
        assertEquals(inverseCallCount.get(), 2);

        safeAction(() -> {
            certificate1.setPersonId(person2Id);
            certificate2.setPersonId(personId);
            assertEquals(certificate1GetPersonCallCount.get(), 1);
            assertEquals(certificate2GetPersonCallCount.get(), 1);
            assertEquals(locatorLookupCallCount.get(), 2);
            assertEquals(inverseCallCount.get(), 2);

            Linkable.link(certificate1);
            assertEquals(locatorLookupCallCount.get(), 3);

            Linkable.link(certificate2);
            assertEquals(locatorLookupCallCount.get(), 4);
        });

        safeAction(() -> assertEquals(certificate1.getPerson(), person2));
        safeAction(() -> assertEquals(person.getCertificate(), certificate2));
        safeAction(() -> assertEquals(person2.getCertificate(), certificate1));

        assertEquals(inverseCallCount.get(), 3);
        assertEquals(certificate1GetPersonCallCount.get(), 2);
    }

    @NullMarked
    @ArezComponent
    abstract static class Certificate {

        static Certificate create(final int personId) {
            return new OneMultiplicityExplicitLoadInverseIntegrationTest_Arez_Certificate(personId);
        }

        @Reference(inverseMultiplicity = Multiplicity.ONE, load = LinkType.EXPLICIT)
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
        abstract Certificate getCertificate();
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class PersonRepository extends AbstractRepository<Integer, Person, PersonRepository> {
        static PersonRepository newRepository() {
            return new OneMultiplicityExplicitLoadInverseIntegrationTest_Arez_PersonRepository();
        }

        @Action
        Person create() {
            final var entity = new OneMultiplicityExplicitLoadInverseIntegrationTest_Arez_Person();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Person entity) {
            super.destroy(entity);
        }
    }
}
