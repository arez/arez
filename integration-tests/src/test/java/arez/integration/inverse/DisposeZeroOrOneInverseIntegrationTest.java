package arez.integration.inverse;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeZeroOrOneInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final DisposeZeroOrOneInverseIntegrationTest_CarRepository repository =
      createCarRepository( locator, locatorLookupCallCount );

    final Car car = repository.create();
    final Car car2 = repository.create();
    final Integer carId = Objects.requireNonNull( Identifiable.getArezId( car ) );
    final Integer car2Id = Objects.requireNonNull( Identifiable.getArezId( car2 ) );

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
    safeAction( () -> assertNull( car2.getWheel() ) );

    assertEquals( wheel1GetCarCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 1 );

    safeAction( () -> wheel1.setCarId( car2Id ) );

    safeAction( () -> assertEquals( wheel1.getCar(), car2 ) );
    safeAction( () -> assertNull( car.getWheel() ) );
    safeAction( () -> assertEquals( car2.getWheel(), wheel1 ) );

    assertEquals( wheel1GetCarCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );

    // Stop observer so we don't get an exception
    Disposable.dispose( inverseObserver );

    Disposable.dispose( car );

    assertTrue( Disposable.isDisposed( car ) );

    Disposable.dispose( car2 );

    assertTrue( Disposable.isDisposed( car2 ) );

    assertInvariant( () -> safeAction( wheel1::getCar ),
                     "Nonnull reference method named 'getCar' invoked on component named 'Wheel.1' but reference has not been resolved yet is not lazy. Id = 2" );
    assertEquals( locatorLookupCallCount.get(), 2 );

    // Stop observer so we don't get an exception
    Disposable.dispose( wheel1GetCarObserver );

    Disposable.dispose( wheel1 );

    assertTrue( Disposable.isDisposed( wheel1 ) );
  }

  @Nonnull
  private DisposeZeroOrOneInverseIntegrationTest_CarRepository createCarRepository( @Nonnull final TypeBasedLocator locator,
                                                                                    @Nonnull final AtomicInteger lookupCallCount )
  {
    final DisposeZeroOrOneInverseIntegrationTest_CarRepository repository =
      DisposeZeroOrOneInverseIntegrationTest_CarRepository.newRepository();

    Arez.context().registerLocator( locator );

    locator.registerLookup( Car.class, id -> {
      lookupCallCount.incrementAndGet();
      return repository.findByArezId( (Integer) id );
    } );
    return repository;
  }

  @ArezComponent
  static abstract class Wheel
  {
    @Nonnull
    static Wheel create( final int carId )
    {
      return new DisposeZeroOrOneInverseIntegrationTest_Arez_Wheel( carId );
    }

    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    @Nonnull
    abstract Car getCar();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getCarId();

    abstract void setCarId( int carId );
  }

  @Repository( sting = Feature.DISABLE, dagger = Feature.DISABLE )
  @ArezComponent
  static abstract class Car
  {
    @Inverse
    @Nullable
    abstract Wheel getWheel();
  }
}
