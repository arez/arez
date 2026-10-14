package arez.integration.references;

import arez.Arez;
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
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ReferenceNotFoundIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var repository = Model2Repository.newRepository();

        final var locator = new TypeBasedLocator();
        Arez.context().registerLocator(locator);

        final Model2 model2 = repository.create();
        final Object model2Id = Objects.requireNonNull(Identifiable.getArezId(model2));

        assertInvariant(
                () -> safeAction(() -> Model1.create(model2Id)),
                "Reference named 'model2' on component named"
                    + " 'arez_integration_references_ReferenceNotFoundIntegrationTest_Model1.1' is unable to resolve"
                    + " entity of type arez.integration.references.ReferenceNotFoundIntegrationTest.Model2 and id ="
                    + " 1");
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {

        private Object _model2Id;

        static Model1 create(final Object model2Id) {
            return new ReferenceNotFoundIntegrationTest_Arez_Model1(model2Id);
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
            return new ReferenceNotFoundIntegrationTest_Arez_Model2Repository();
        }

        @Action
        Model2 create() {
            final var entity = new ReferenceNotFoundIntegrationTest_Arez_Model2();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Model2 entity) {
            super.destroy(entity);
        }
    }
}
