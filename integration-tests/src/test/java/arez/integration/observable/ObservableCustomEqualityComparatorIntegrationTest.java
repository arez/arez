package arez.integration.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObservableCustomEqualityComparatorIntegrationTest
  extends AbstractArezIntegrationTest
{
  static final class CaseInsensitiveComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      if ( null == oldValue || null == newValue )
      {
        return oldValue == newValue;
      }
      return oldValue.toString().equalsIgnoreCase( newValue.toString() );
    }
  }

  @ArezComponent
  static abstract class Model
  {
    @Nonnull
    private String _name = "";

    @Observable( setterAlwaysMutates = false, equalityComparator = CaseInsensitiveComparator.class )
    @Nonnull
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name.trim();
    }
  }

  @Test
  public void scenario()
  {
    final Model model = new ObservableCustomEqualityComparatorIntegrationTest_Arez_Model();
    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      model.getName();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    safeAction( () -> model.setName( "Abc" ) );

    assertEquals( callCount.get(), 2 );

    safeAction( () -> model.setName( "aBC" ) );

    assertEquals( callCount.get(), 2 );

    safeAction( () -> model.setName( "Abc   " ) );

    assertEquals( callCount.get(), 2 );

    safeAction( () -> model.setName( "Def" ) );

    assertEquals( callCount.get(), 3 );
  }
}
