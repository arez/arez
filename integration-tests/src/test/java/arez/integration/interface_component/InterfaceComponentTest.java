package arez.integration.interface_component;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class InterfaceComponentTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  interface TestComponent
  {
    @Observable
    void setMyVar( int value );

    int getMyVar();

    @Memoize
    default boolean isOver2()
    {
      return getMyVar() > 2;
    }
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new InterfaceComponentTest_Arez_TestComponent();
    context.safeAction( () -> component.setMyVar( 1 ) );

    assertEquals( context.safeAction( component::getMyVar ).intValue(), 1 );
    assertFalse( context.safeAction( component::isOver2 ) );

    context.safeAction( () -> component.setMyVar( 33 ) );

    assertEquals( context.safeAction( component::getMyVar ).intValue(), 33 );
    assertTrue( context.safeAction( component::isOver2 ) );
  }
}
