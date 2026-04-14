package arez.integration.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DefaultEqualityComparatorMemoizeIntegrationTest
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
    private int _value;

    @Observable
    int getValue()
    {
      return _value;
    }

    void setValue( final int value )
    {
      _value = value;
    }

    @Memoize
    Name getDefaultName()
    {
      return new Name( 0 == ( getValue() % 2 ) ? "Abc" : "aBC" );
    }

    @Memoize( equalityComparator = ExactComparator.class )
    Name getExplicitName()
    {
      return new Name( 0 == ( getValue() % 2 ) ? "Abc" : "aBC" );
    }
  }

  @Test
  public void scenario()
  {
    final Model model = new DefaultEqualityComparatorMemoizeIntegrationTest_Arez_Model();
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

    safeAction( () -> model.setValue( 1 ) );

    assertEquals( defaultCount.get(), 1 );
    assertEquals( explicitCount.get(), 2 );

    safeAction( () -> model.setValue( 2 ) );

    assertEquals( defaultCount.get(), 1 );
    assertEquals( explicitCount.get(), 3 );
  }
}
