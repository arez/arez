package org.realityforge.arez.integration;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.ComputedValueRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent
  static class TestComponent
  {
    private int _otherID;

    @ComputedValueRef
    ComputedValue<Integer> getOtherIDComputedValue()
    {
      throw new IllegalStateException();
    }

    String getOther()
    {
      return String.valueOf( getOtherIDComputedValue().get() );
    }

    @Computed
    int getOtherID()
    {
      return _otherID;
    }

    void setOtherID( final int otherID )
    {
      _otherID = otherID;
    }
  }

  @Test
  public void ref()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ComputedValueRefTest_Arez_TestComponent();
    component.setOtherID( 1 );

    final Integer valueAsInt = context.action( component::getOtherID );
    final String valueAsString = context.action( component::getOther );

    assertEquals( valueAsInt.intValue(), 1 );
    assertEquals( valueAsString, "1" );
  }
}
