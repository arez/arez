package arez.integration.inverse;

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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ManyMultiplicityInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final CarRepository repository = CarRepository.newRepository();

    Arez.context().registerLocator( locator );

    locator.registerLookup( Car.class, id -> {
      locatorLookupCallCount.incrementAndGet();
      return repository.findByArezId( (Integer) id );
    } );

    final Car car = repository.create();
    final Car car2 = repository.create();
    final Integer carId = Objects.requireNonNull( Identifiable.getArezId( car ) );
    final Integer car2Id = Objects.requireNonNull( Identifiable.getArezId( car2 ) );

    assertEquals( locatorLookupCallCount.get(), 0 );

    final AtomicInteger inverseCallCount = new AtomicInteger();
    observer( () -> {
      car.getWheels();
      inverseCallCount.incrementAndGet();
    } );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );
    final Wheel wheel1 = safeAction( () -> Wheel.create( carId ) );
    assertEquals( locatorLookupCallCount.get(), 1 );
    assertEquals( inverseCallCount.get(), 2 );

    final AtomicInteger wheel1GetCarCallCount = new AtomicInteger();
    final Observer wheel1GetCarObserver = observer( () -> {
      wheel1.getCar();
      wheel1GetCarCallCount.incrementAndGet();
    } );

    safeAction( () -> assertEquals( wheel1.getCar(), car ) );
    safeAction( () -> assertEquals( car.getWheels().size(), 1 ) );
    safeAction( () -> assertTrue( car.getWheels().contains( wheel1 ) ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 0 ) );

    assertEquals( wheel1GetCarCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 1 );

    safeAction( () -> wheel1.setCarId( car2Id ) );

    safeAction( () -> assertEquals( wheel1.getCar(), car2 ) );
    safeAction( () -> assertEquals( car.getWheels().size(), 0 ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 1 ) );
    safeAction( () -> assertTrue( car2.getWheels().contains( wheel1 ) ) );

    assertEquals( inverseCallCount.get(), 3 );
    assertEquals( wheel1GetCarCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );

    // Stop observer so we don't get an exception
    Disposable.dispose( wheel1GetCarObserver );

    Disposable.dispose( wheel1 );

    assertTrue( Disposable.isDisposed( wheel1 ) );
    safeAction( () -> assertEquals( car.getWheels().size(), 0 ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 0 ) );

    assertEquals( wheel1GetCarCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );
  }

  @ArezComponent
  static abstract class Wheel
  {
    @Nonnull
    static Wheel create( final int carId )
    {
      return new ManyMultiplicityInverseIntegrationTest_Arez_Wheel( carId );
    }

    @Reference( inverse = Feature.ENABLE )
    abstract Car getCar();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getCarId();

    abstract void setCarId( int carId );
  }

  @ArezComponent( observable = Feature.ENABLE )
  static abstract class Car
  {
    @Inverse
    abstract Collection<Wheel> getWheels();
  }

  @ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE )
  static abstract class CarRepository
    extends AbstractRepository<Integer, Car, CarRepository>
  {
    static CarRepository newRepository()
    {
      return new ManyMultiplicityInverseIntegrationTest_Arez_CarRepository();
    }

    @Action
    Car create()
    {
      final ManyMultiplicityInverseIntegrationTest_Arez_Car entity =
        new ManyMultiplicityInverseIntegrationTest_Arez_Car();
      attach( entity );
      return entity;
    }

    @Action
    protected void destroy( @Nonnull final Car entity )
    {
      super.destroy( entity );
    }
  }
}
