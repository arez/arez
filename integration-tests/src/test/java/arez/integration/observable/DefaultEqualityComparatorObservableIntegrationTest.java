package arez.integration.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DefaultEqualityComparatorObservableIntegrationTest
  extends AbstractArezIntegrationTest
{
  @DefaultEqualityComparator( CaseInsensitiveComparator.class )
  static final class Name
  {
    private final String _value;

    Name( final String value )
    {
      _value = value;
    }

    @Override
    public String toString()
    {
      return _value;
    }
  }

  static final class CaseInsensitiveComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equalsIgnoreCase( newValue.toString() );
    }
  }

  static final class ExactComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equals( newValue.toString() );
    }
  }

  @ArezComponent
  static abstract class Model
  {
    private Name _defaultName = new Name( "" );
    private Name _explicitName = new Name( "" );

    @Observable( setterAlwaysMutates = false )
    Name getDefaultName()
    {
      return _defaultName;
    }

    void setDefaultName( final Name defaultName )
    {
      _defaultName = defaultName;
    }

    @Observable( setterAlwaysMutates = false, equalityComparator = ExactComparator.class )
    Name getExplicitName()
    {
      return _explicitName;
    }

    void setExplicitName( final Name explicitName )
    {
      _explicitName = explicitName;
    }
  }

  @Test
  public void scenario()
  {
    final Model model = new DefaultEqualityComparatorObservableIntegrationTest_Arez_Model();
    final AtomicInteger defaultCount = new AtomicInteger();
    final AtomicInteger explicitCount = new AtomicInteger();

    observer( () -> {
      model.getDefaultName();
      defaultCount.incrementAndGet();
    } );
    observer( () -> {
      model.getExplicitName();
      explicitCount.incrementAndGet();
    } );

    assertEquals( defaultCount.get(), 1 );
    assertEquals( explicitCount.get(), 1 );

    safeAction( () -> model.setDefaultName( new Name( "Abc" ) ) );
    safeAction( () -> model.setExplicitName( new Name( "Abc" ) ) );

    assertEquals( defaultCount.get(), 2 );
    assertEquals( explicitCount.get(), 2 );

    safeAction( () -> model.setDefaultName( new Name( "aBC" ) ) );
    safeAction( () -> model.setExplicitName( new Name( "aBC" ) ) );

    assertEquals( defaultCount.get(), 2 );
    assertEquals( explicitCount.get(), 3 );
  }
}
