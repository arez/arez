package arez.integration.references;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.LinkType;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class NullablelImmutableExplicitReferenceIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var repository = Model2Repository.newRepository();

        final var locator = new TypeBasedLocator();
        Arez.context().registerLocator(locator);

        final var findCallCount = new AtomicInteger();
        final HashMap<Object, Model2> entities = new HashMap<>();
        locator.registerLookup(Model2.class, id -> {
            findCallCount.incrementAndGet();
            return entities.get(id);
        });

        final Model2 model2a = repository.create();
        final Object model2aId = Objects.requireNonNull(Identifiable.getArezId(model2a));
        entities.put(model2aId, model2a);

        assertEquals(findCallCount.get(), 0);
        final var model1 = Model1.create(model2aId);
        assertEquals(findCallCount.get(), 0);

        assertInvariant(
                model1::getModel2,
                "Nullable reference method named 'getModel2' invoked on component named"
                    + " 'arez_integration_references_NullablelImmutableExplicitReferenceIntegrationTest_Model1.1' and"
                    + " reference has not been resolved yet is not lazy. Id = 1");

        Linkable.link(model1);

        assertEquals(findCallCount.get(), 1);

        assertEquals(model1.getModel2(), model2a);

        assertEquals(findCallCount.get(), 1);

        Disposable.dispose(model1);

        assertInvariant(
                model1::getModel2,
                "Method named 'getModel2' invoked on disposed component named"
                        + " 'arez_integration_references_NullablelImmutableExplicitReferenceIntegrationTest_Model1.1'");

        findCallCount.set(0);

        final var model1b = Model1.create(null);
        assertEquals(findCallCount.get(), 0);

        assertNull(model1b.getModel2());

        assertEquals(findCallCount.get(), 0);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {
        @Nullable
        private final Object _model2Id;

        static Model1 create(@Nullable final Object model2Id) {
            return new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model1(model2Id);
        }

        Model1(@Nullable final Object model2Id) {
            _model2Id = model2Id;
        }

        @Reference(load = LinkType.EXPLICIT)
        abstract @Nullable Model2 getModel2();

        @ReferenceId
        @Nullable
        Object getModel2Id() {
            return _model2Id;
        }
    }

    @NullMarked
    @ArezComponent(requireId = Feature.ENABLE)
    abstract static class Model2 {
        @Action
        void doStuff() {}
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class Model2Repository extends AbstractRepository<Integer, Model2, Model2Repository> {
        static Model2Repository newRepository() {
            return new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model2Repository();
        }

        @Action
        Model2 create() {
            final var entity = new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model2();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Model2 entity) {
            super.destroy(entity);
        }
    }
}
