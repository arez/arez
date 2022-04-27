package arez.integration.inverse;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DisposeOneInverseIntegrationTest
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
    final Integer carId = Objects.requireNonNull( Identifiable.getArezId( car ) );

    assertEquals( locatorLookupCallCount.get(), 0 );

    final AtomicInteger inverseCallCount = new AtomicInteger();
    final Observer inverseObserver = observer( () -> {
      car.getWheel();
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
    safeAction( () -> assertEquals( car.getWheel(), wheel1 ) );

    assertEquals( wheel1GetCarCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 1 );

    // Stop observer so we don't get an exception
    Disposable.dispose( inverseObserver );

    Disposable.dispose( car );

    assertTrue( Disposable.isDisposed( car ) );

    assertInvariant( () -> safeAction( wheel1::getCar ),
                     "Nonnull reference method named 'getCar' invoked on component named 'arez_integration_inverse_DisposeOneInverseIntegrationTest_Wheel.1' but reference has not been resolved yet is not lazy. Id = 1" );
    assertEquals( locatorLookupCallCount.get(), 1 );

    // Stop observer so we don't get an exception
    Disposable.dispose( wheel1GetCarObserver );

    Disposable.dispose( wheel1 );

    assertTrue( Disposable.isDisposed( wheel1 ) );
  }

  @ArezComponent
  static abstract class Wheel
  {
    @Nonnull
    static Wheel create( final int carId )
    {
      return new DisposeOneInverseIntegrationTest_Arez_Wheel( carId );
    }

    @Reference( inverseMultiplicity = Multiplicity.ONE )
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
    @Nonnull
    abstract Wheel getWheel();
  }

  @ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE )
  static abstract class CarRepository
    extends AbstractRepository<Integer, Car, CarRepository>
  {
    static CarRepository newRepository()
    {
      return new DisposeOneInverseIntegrationTest_Arez_CarRepository();
    }

    @Action
    Car create()
    {
      final DisposeOneInverseIntegrationTest_Arez_Car entity = new DisposeOneInverseIntegrationTest_Arez_Car();
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
