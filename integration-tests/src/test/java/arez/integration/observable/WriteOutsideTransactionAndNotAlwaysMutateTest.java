package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WriteOutsideTransactionAndNotAlwaysMutateTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Nonnull
    private String _name = "";

    @Observable( writeOutsideTransaction = Feature.ENABLE, setterAlwaysMutates = false )
    @Nonnull
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull String name )
    {
      _name = name.trim();
    }
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  @Test
  public void scenario()
  {
    final MyComponent component = new WriteOutsideTransactionAndNotAlwaysMutateTest_Arez_MyComponent();

    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      component.getName();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    // Write outside transaction
    component.setName( "ABC" );

    // If gets here it reportObserved so observer re-ran
    assertEquals( callCount.get(), 2 );

    safeAction( () -> assertEquals( component.getName(), "ABC" ) );

    // Write inside transaction
    safeAction( () -> component.setName( "DEF" ) );

    assertEquals( callCount.get(), 3 );

    safeAction( () -> assertEquals( component.getName(), "DEF" ) );

    // Write but produce no changes
    component.setName( "    DEF    " );

    assertEquals( callCount.get(), 3 );

    safeAction( () -> assertEquals( component.getName(), "DEF" ) );
  }
}
