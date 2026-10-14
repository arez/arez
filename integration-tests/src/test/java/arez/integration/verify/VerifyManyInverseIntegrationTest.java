package arez.integration.verify;

import static org.testng.Assert.*;

import arez.Arez;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.TypeBasedLocator;
import arez.component.Verifiable;
import arez.component.internal.AbstractRepository;
import arez.component.internal.ComponentKernel;
import arez.integration.AbstractArezIntegrationTest;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class VerifyManyInverseIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void verifySuccess() throws Throwable {
        final Model2Repository repository = Model2Repository.newRepository();

        final TypeBasedLocator locator = new TypeBasedLocator();
        Arez.context().registerLocator(locator);

        final HashMap<Object, VerifyManyInverseIntegrationTest.Model1> entities1 = new HashMap<>();
        final HashMap<Object, VerifyManyInverseIntegrationTest.Model2> entities2 = new HashMap<>();
        locator.registerLookup(VerifyManyInverseIntegrationTest.Model1.class, entities1::get);
        locator.registerLookup(VerifyManyInverseIntegrationTest.Model2.class, entities2::get);

        final VerifyManyInverseIntegrationTest.Model2 model2 = repository.create(1);
        entities2.put(model2.getId(), model2);

        final VerifyManyInverseIntegrationTest.Model1 model1 =
                VerifyManyInverseIntegrationTest.Model1.create(0, model2.getId());
        entities1.put(model1.getId(), model1);

        assertTrue(model1 instanceof Verifiable);
        assertTrue(model2 instanceof Verifiable);

        Verifiable.verify(model1);
        Verifiable.verify(model2);
    }

    @Test
    public void unableToLocateOther() throws Throwable {
        final Model2Repository repository = Model2Repository.newRepository();

        final TypeBasedLocator locator = new TypeBasedLocator();
        Arez.context().registerLocator(locator);

        final HashMap<Object, VerifyManyInverseIntegrationTest.Model1> entities1 = new HashMap<>();
        final HashMap<Object, VerifyManyInverseIntegrationTest.Model2> entities2 = new HashMap<>();
        locator.registerLookup(VerifyManyInverseIntegrationTest.Model1.class, entities1::get);
        locator.registerLookup(VerifyManyInverseIntegrationTest.Model2.class, entities2::get);

        final VerifyManyInverseIntegrationTest.Model2 model2 = repository.create(1);
        entities2.put(model2.getId(), model2);

        final VerifyManyInverseIntegrationTest.Model1 model1 =
                VerifyManyInverseIntegrationTest.Model1.create(0, model2.getId());
        entities1.put(model1.getId(), model1);

        assertTrue(model1 instanceof Verifiable);
        assertTrue(model2 instanceof Verifiable);

        Verifiable.verify(model1);

        Verifiable.verify(model2);

        final Field kernelField = model1.getClass().getDeclaredField("$$arezi$$_kernel");
        kernelField.setAccessible(true);
        final ComponentKernel kernel = (ComponentKernel) kernelField.get(model1);

        final Field field = kernel.getClass().getDeclaredField("_state");
        field.setAccessible(true);
        field.set(kernel, (byte) -1);

        assertInvariant(
                () -> Verifiable.verify(model2),
                "Inverse relationship named 'model1s' on component named"
                    + " 'arez_integration_verify_VerifyManyInverseIntegrationTest_Model2.1' contains disposed element"
                    + " 'ArezComponent[arez_integration_verify_VerifyManyInverseIntegrationTest_Model1.0]'");
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {
        private final int _id;
        private final int _model2Id;

        static @Nullable Model1 create(final int id, final int model2Id) {
            return Arez.context().safeAction(() -> new VerifyManyInverseIntegrationTest_Arez_Model1(id, model2Id));
        }

        Model1(final int id, final int model2Id) {
            _id = id;
            _model2Id = model2Id;
        }

        @ComponentId
        int getId() {
            return _id;
        }

        @Reference(inverseMultiplicity = Multiplicity.MANY)
        abstract @Nullable Model2 getModel2();

        @ReferenceId
        int getModel2Id() {
            return _model2Id;
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model2 {
        private final int _id;

        Model2(final int id) {
            _id = id;
        }

        @ComponentId
        int getId() {
            return _id;
        }

        @Inverse
        abstract @Nullable Collection<Model1> getModel1s();
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class Model2Repository extends AbstractRepository<Integer, Model2, Model2Repository> {

        static Model2Repository newRepository() {
            return new VerifyManyInverseIntegrationTest_Arez_Model2Repository();
        }

        @Action
        @Nullable
        Model2 create(final int id) {
            final VerifyManyInverseIntegrationTest_Arez_Model2 entity =
                    new VerifyManyInverseIntegrationTest_Arez_Model2(id);
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Model2 entity) {
            super.destroy(entity);
        }
    }
}
