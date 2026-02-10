package arez.integration.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MemoizeCustomEqualityComparatorIntegrationTest
  extends AbstractArezIntegrationTest
{
  static final class AlwaysEqualComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return true;
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

    @Memoize( equalityComparator = AlwaysEqualComparator.class )
    int getMemoizedValue()
    {
      return getValue();
    }
  }

  @Test
  public void scenario()
  {
    final Model model = new MemoizeCustomEqualityComparatorIntegrationTest_Arez_Model();
    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      model.getMemoizedValue();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    safeAction( () -> model.setValue( 1 ) );

    assertEquals( callCount.get(), 1 );

    safeAction( () -> model.setValue( 2 ) );

    assertEquals( callCount.get(), 1 );
  }
}
