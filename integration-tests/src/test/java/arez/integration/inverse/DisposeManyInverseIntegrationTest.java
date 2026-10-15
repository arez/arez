package arez.integration.inverse;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class DisposeManyInverseIntegrationTest extends AbstractArezIntegrationTest {
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

        final Car car = repository.create();
        final Integer carId = Identifiable.getArezId(car);
        assertNotNull(carId);
        assert null != carId;

        assertEquals(locatorLookupCallCount.get(), 0);

        final var inverseCallCount = new AtomicInteger();
        final Observer inverseObserver = observer(() -> {
            car.getWheels();
            inverseCallCount.incrementAndGet();
        });

        assertEquals(locatorLookupCallCount.get(), 0);
        assertEquals(inverseCallCount.get(), 1);
        final Wheel wheel1 = safeAction(() -> Wheel.create(carId));
        assertEquals(locatorLookupCallCount.get(), 1);
        assertEquals(inverseCallCount.get(), 2);

        final var wheel1GetCarCallCount = new AtomicInteger();
        final Observer wheel1GetCarObserver = observer(() -> {
            wheel1.getCar();
            wheel1GetCarCallCount.incrementAndGet();
        });

        safeAction(() -> assertEquals(wheel1.getCar(), car));
        safeAction(() -> assertEquals(car.getWheels().size(), 1));
        safeAction(() -> assertTrue(car.getWheels().contains(wheel1)));

        assertEquals(wheel1GetCarCallCount.get(), 1);
        assertEquals(locatorLookupCallCount.get(), 1);

        // Stop observer so we don't get an exception
        Disposable.dispose(inverseObserver);

        Disposable.dispose(car);

        assertTrue(Disposable.isDisposed(car));

        assertInvariant(
                () -> safeAction(wheel1::getCar),
                "Nonnull reference method named 'getCar' invoked on component named"
                        + " 'arez_integration_inverse_DisposeManyInverseIntegrationTest_Wheel.1' but reference has not"
                        + " been resolved yet is not lazy. Id = 1");
        assertEquals(locatorLookupCallCount.get(), 1);

        // Stop observer so we don't get an exception
        Disposable.dispose(wheel1GetCarObserver);

        Disposable.dispose(wheel1);

        assertTrue(Disposable.isDisposed(wheel1));
    }

    @NullMarked
    @ArezComponent
    abstract static class Wheel {

        static Wheel create(final int carId) {
            return new DisposeManyInverseIntegrationTest_Arez_Wheel(carId);
        }

        @Reference(inverse = Feature.ENABLE)
        abstract @Nullable Car getCar();

        @ReferenceId
        @Observable(initializer = Feature.ENABLE)
        abstract int getCarId();

        abstract void setCarId(int carId);
    }

    @NullMarked
    @ArezComponent(observable = Feature.ENABLE)
    abstract static class Car {
        @Inverse
        abstract Collection<Wheel> getWheels();
    }

    @NullMarked
    @ArezComponent(service = Feature.ENABLE, sting = Feature.DISABLE)
    abstract static class CarRepository extends AbstractRepository<Integer, Car, CarRepository> {
        static CarRepository newRepository() {
            return new DisposeManyInverseIntegrationTest_Arez_CarRepository();
        }

        @Action
        Car create() {
            final var entity = new DisposeManyInverseIntegrationTest_Arez_Car();
            attach(entity);
            return entity;
        }

        @Action
        protected void destroy(final Car entity) {
            super.destroy(entity);
        }
    }
}
