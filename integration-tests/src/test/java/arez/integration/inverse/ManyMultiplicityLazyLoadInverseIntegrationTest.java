package arez.integration.inverse;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ManyMultiplicityLazyLoadInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final ManyMultiplicityLazyLoadInverseIntegrationTest_CarRepository repository =
      createCarRepository( locator, locatorLookupCallCount );

    final Car car = repository.create();
    final Car car2 = repository.create();
    final Integer carId = Objects.requireNonNull( Identifiable.getArezId( car ) );
    final Integer car2Id = Objects.requireNonNull( Identifiable.getArezId( car2 ) );

    assertEquals( locatorLookupCallCount.get(), 0 );

    final AtomicInteger inverseCallCount = new AtomicInteger();
    autorun( () -> {
      car.getWheels();
      inverseCallCount.incrementAndGet();
    } );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );

    final Wheel wheel1 = Wheel.create( carId );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );

    // It is lazy so not added to inverse until lazy resolved
    safeAction( () -> assertEquals( car.getWheels().size(), 0 ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 0 ) );

    // resolve the lazy
    safeAction( () -> assertEquals( wheel1.getCar(), car ) );
    safeAction( () -> assertEquals( car.getWheels().size(), 1 ) );
    safeAction( () -> assertEquals( car.getWheels().contains( wheel1 ), true ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 0 ) );

    assertEquals( locatorLookupCallCount.get(), 1 );
    assertEquals( inverseCallCount.get(), 2 );

    final AtomicInteger wheel1GetCarCallCount = new AtomicInteger();

    final Observer wheel1GetCarObserver = autorun( () -> {
      wheel1.getCar();
      wheel1GetCarCallCount.incrementAndGet();
    } );

    assertEquals( wheel1GetCarCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 1 );

    // This will trigger observer so no need to explicitly resolve
    safeAction( () -> {
      wheel1.setCarId( car2Id );
      assertEquals( wheel1GetCarCallCount.get(), 1 );
      assertEquals( locatorLookupCallCount.get(), 1 );

      // Removed from inverses
      assertEquals( car.getWheels().size(), 0 );
      assertEquals( car2.getWheels().size(), 0 );

      // Resolve the lazy reference
      assertEquals( wheel1.getCar(), car2 );
    } );


    safeAction( () -> assertEquals( car.getWheels().size(), 0 ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 1 ) );
    safeAction( () -> assertEquals( car2.getWheels().contains( wheel1 ), true ) );

    assertEquals( inverseCallCount.get(), 3 );
    assertEquals( wheel1GetCarCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );

    // Stop observer so we don't get an exception
    Disposable.dispose( wheel1GetCarObserver );

    Disposable.dispose( wheel1 );

    assertEquals( Disposable.isDisposed( wheel1 ), true );
    safeAction( () -> assertEquals( car.getWheels().size(), 0 ) );
    safeAction( () -> assertEquals( car2.getWheels().size(), 0 ) );

    assertEquals( wheel1GetCarCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );
  }

  @Nonnull
  private ManyMultiplicityLazyLoadInverseIntegrationTest_CarRepository createCarRepository( @Nonnull final TypeBasedLocator locator,
                                                                                            @Nonnull final AtomicInteger lookupCallCount )
  {
    final ManyMultiplicityLazyLoadInverseIntegrationTest_CarRepository repository =
      ManyMultiplicityLazyLoadInverseIntegrationTest_CarRepository.newRepository();

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
      return new ManyMultiplicityLazyLoadInverseIntegrationTest_Arez_Wheel( carId );
    }

    @Reference( inverse = Feature.ENABLE, load = LinkType.LAZY )
    abstract Car getCar();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getCarId();

    abstract void setCarId( int carId );
  }

  @Repository
  @ArezComponent
  static abstract class Car
  {
    @Inverse
    abstract Collection<Wheel> getWheels();
  }
}
