package arez.integration.inverse;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.PostInverseAdd;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class InverseHooksIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var locatorLookupCallCount = new AtomicInteger();

        final var locator = new TypeBasedLocator();
        final var repository = CarRepository.newRepository();

        Arez.context().registerLocator(locator);

        locator.registerLookup(Car.class, id -> {
            locatorLookupCallCount.incrementAndGet();
            return repository.findByArezId((Integer) id);
        });
        final List<String> trace = new ArrayList<>();
        final Car car = repository.create("P", trace);
        final Car car2 = repository.create("Q", trace);
        final Integer carId = Identifiable.getArezId(car);
        assertNotNull(carId);
        assert null != carId;
        final Integer car2Id = Identifiable.getArezId(car2);
        assertNotNull(car2Id);
        assert null != car2Id;

        assertEquals(locatorLookupCallCount.get(), 0);

        final var inverseCallCount = new AtomicInteger();
        observer(() -> {
            car.getWheels();
            inverseCallCount.incrementAndGet();
        });

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);
        final Wheel wheel1 = safeAction(() -> Wheel.create("A", carId));
        safeAction(() -> Wheel.create("B", carId));
        safeAction(() -> Wheel.create("C", carId));
        assertEquals(locatorLookupCallCount.get(), 3);
        assertEquals(inverseCallCount.get(), 4);

        assertEquals(car.getWheels().size(), 3);
        assertEquals(car2.getWheels().size(), 0);

        safeAction(() -> wheel1.setCarId(car2Id));

        assertEquals(car.getWheels().size(), 2);
        assertEquals(car2.getWheels().size(), 1);

        Disposable.dispose(wheel1);

        assertTrue(Disposable.isDisposed(wheel1));
        assertEquals(car.getWheels().size(), 2);
        assertEquals(car2.getWheels().size(), 0);

        assertEquals(String.join("|", trace), "P+A|P+B|P+C|P-A|Q+A|Q-A");
    }

    @NullMarked
    @ArezComponent(defaultReadOutsideTransaction = Feature.ENABLE)
    abstract static class Wheel {

        private final String _name;

        Wheel(final String name) {
            _name = name;
        }

        static Wheel create(final String name, final int carId) {
            return new InverseHooksIntegrationTest_Arez_Wheel(name, carId);
        }

        String getName() {
            return _name;
        }

        @Reference(inverse = Feature.ENABLE)
        abstract @Nullable Car getCar();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        abstract int getCarId();

        abstract void setCarId(int carId);
    }

    @NullMarked
    @ArezComponent(defaultReadOutsideTransaction = Feature.ENABLE, observable = Feature.ENABLE)
    abstract static class Car {

        private final String _name;

        private final List<String> _trace;

        Car(final String name, final List<String> trace) {
            _name = name;
            _trace = trace;
        }

        @PreInverseRemove
        void preWheelsRemove(final Wheel wheel) {
            _trace.add(_name + "-" + wheel.getName());
        }

        @PostInverseAdd
        void postWheelsAdd(final Wheel wheel) {
            _trace.add(_name + "+" + wheel.getName());
        }

        @Inverse
        abstract Collection<Wheel> getWheels();
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class CarRepository extends AbstractRepository<Integer, Car, CarRepository> {
        static CarRepository newRepository() {
            return new InverseHooksIntegrationTest_Arez_CarRepository();
        }

        @Action
        Car create(final String name, final List<String> trace) {
            final var entity = new InverseHooksIntegrationTest_Arez_Car(name, trace);
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Car entity) {
            super.destroy(entity);
        }
    }
}
