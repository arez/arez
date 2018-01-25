package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    private int _otherID;

    @ComputedValueRef
    abstract ComputedValue<Integer> getOtherIDComputedValue();

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
