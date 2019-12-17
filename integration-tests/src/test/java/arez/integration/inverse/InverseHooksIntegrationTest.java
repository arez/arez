package arez.integration.inverse;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.PostInverseAdd;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class InverseHooksIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final InverseHooksIntegrationTest_CarRepository repository =
      createCarRepository( locator, locatorLookupCallCount );
    final List<String> trace = new ArrayList<>();
    final Car car = repository.create( "P", trace );
    final Car car2 = repository.create( "Q", trace );
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
    final Wheel wheel1 = safeAction( () -> Wheel.create( "A", carId ) );
    safeAction( () -> Wheel.create( "B", carId ) );
    safeAction( () -> Wheel.create( "C", carId ) );
    assertEquals( locatorLookupCallCount.get(), 3 );
    assertEquals( inverseCallCount.get(), 4 );

    assertEquals( car.getWheels().size(), 3 );
    assertEquals( car2.getWheels().size(), 0 );

    safeAction( () -> wheel1.setCarId( car2Id ) );

    assertEquals( car.getWheels().size(), 2 );
    assertEquals( car2.getWheels().size(), 1 );

    Disposable.dispose( wheel1 );

    assertTrue( Disposable.isDisposed( wheel1 ) );
    assertEquals( car.getWheels().size(), 2 );
    assertEquals( car2.getWheels().size(), 0 );

    assertEquals( String.join( "|", trace ), "P+A|P+B|P+C|P-A|Q+A|Q-A" );
  }

  @Nonnull
  private InverseHooksIntegrationTest_CarRepository createCarRepository( @Nonnull final TypeBasedLocator locator,
                                                                         @Nonnull final AtomicInteger lookupCallCount )
  {
    final InverseHooksIntegrationTest_CarRepository repository =
      InverseHooksIntegrationTest_CarRepository.newRepository();

    Arez.context().registerLocator( locator );

    locator.registerLookup( Car.class, id -> {
      lookupCallCount.incrementAndGet();
      return repository.findByArezId( (Integer) id );
    } );
    return repository;
  }

  @ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE )
  static abstract class Wheel
  {
    @Nonnull
    private final String _name;

    Wheel( @Nonnull final String name )
    {
      _name = name;
    }

    @Nonnull
    static Wheel create( @Nonnull final String name, final int carId )
    {
      return new InverseHooksIntegrationTest_Arez_Wheel( name, carId );
    }

    @Nonnull
    String getName()
    {
      return _name;
    }

    @Reference( inverse = Feature.ENABLE )
    abstract Car getCar();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getCarId();

    abstract void setCarId( int carId );
  }

  @Repository
  @ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE )
  static abstract class Car
  {
    @Nonnull
    private final String _name;
    @Nonnull
    private final List<String> _trace;

    Car( @Nonnull final String name, @Nonnull final List<String> trace )
    {
      _name = name;
      _trace = trace;
    }

    @PreInverseRemove
    final void preWheelsRemove( @Nonnull final Wheel wheel )
    {
      _trace.add( _name + "-" + wheel.getName() );
    }

    @PostInverseAdd
    final void postWheelsAdd( @Nonnull final Wheel wheel )
    {
      _trace.add( _name + "+" + wheel.getName() );
    }

    @Inverse
    abstract Collection<Wheel> getWheels();
  }
}
