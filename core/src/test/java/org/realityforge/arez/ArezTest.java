package org.realityforge.arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArezTest
  extends AbstractArezTest
{
  @Test
  public void context_defaults()
  {
    final ArezContext context1 = Arez.context();
    assertNotNull( context1 );
    final ArezContext context2 = Arez.context();
    assertTrue( context1 == context2 );
    assertTrue( Arez.getContextProvider() instanceof Arez.StaticContextProvider );
  }

  @Test
  public void context_customProvider()
  {
    final ArezContext context = new ArezContext();
    Arez.bindProvider( () -> context );

    assertTrue( context == Arez.context() );
    assertFalse( Arez.getContextProvider() instanceof Arez.StaticContextProvider );
  }

  @Test
  public void context_failedToRebind()
  {
    final ArezContext context = new ArezContext();
    final Arez.ContextProvider provider1 = () -> context;
    final Arez.ContextProvider provider2 = () -> context;
    Arez.bindProvider( provider1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Arez.bindProvider( provider2 ) );

    assertEquals( exception.getMessage(),
                  "Attempting to bind ContextProvider " + provider2 + " but there is already a " +
                  "provider bound as " + provider1 + "." );

  }
}
