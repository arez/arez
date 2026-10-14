package arez.integration.references;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
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
public final class NonnullObservableEagerReferenceIntegrationTest extends AbstractArezIntegrationTest {
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
        final Model2 model2b = repository.create();
        final Object model2aId = Objects.requireNonNull(Identifiable.getArezId(model2a));
        final Object model2bId = Objects.requireNonNull(Identifiable.getArezId(model2b));
        entities.put(model2aId, model2a);
        entities.put(model2bId, model2b);

        assertEquals(findCallCount.get(), 0);
        final Model1 model1 = safeAction(() -> Model1.create(model2aId));
        assertEquals(findCallCount.get(), 1);

        safeAction(() -> assertEquals(model1.getModel2(), model2a));

        assertEquals(findCallCount.get(), 1);

        final var observerCallCount = new AtomicInteger();
        final Observer observer = observer(() -> {
            model1.getModel2();
            observerCallCount.incrementAndGet();
        });

        assertEquals(observerCallCount.get(), 1);
        assertEquals(findCallCount.get(), 1);

        safeAction(() -> model1.setModel2Id(model2bId));

        assertEquals(observerCallCount.get(), 2);
        assertEquals(findCallCount.get(), 2);

        safeAction(() -> assertEquals(model1.getModel2(), model2b));

        assertEquals(observerCallCount.get(), 2);
        assertEquals(findCallCount.get(), 2);

        // Shutdown to avoid errors
        observer.dispose();

        Disposable.dispose(model1);

        assertInvariant(
                model1::getModel2,
                "Method named 'getModel2' invoked on disposed component named"
                        + " 'arez_integration_references_NonnullObservableEagerReferenceIntegrationTest_Model1.1'");
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {

        private Object _model2Id;

        static Model1 create(final Object model2Id) {
            return new NonnullObservableEagerReferenceIntegrationTest_Arez_Model1(model2Id);
        }

        Model1(final Object model2Id) {
            _model2Id = model2Id;
        }

        @Reference
        abstract @Nullable Model2 getModel2();

        @ReferenceId
        @Observable
        Object getModel2Id() {
            return _model2Id;
        }

        void setModel2Id(Object model2Id) {
            _model2Id = model2Id;
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
            return new NonnullObservableEagerReferenceIntegrationTest_Arez_Model2Repository();
        }

        @Action
        Model2 create() {
            final var entity = new NonnullObservableEagerReferenceIntegrationTest_Arez_Model2();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Model2 entity) {
            super.destroy(entity);
        }
    }
}
