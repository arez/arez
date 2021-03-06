package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputableValueRefTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    private int _otherID;

    @ComputableValueRef
    abstract ComputableValue<Integer> getOtherIDComputableValue();

    String getOther()
    {
      return String.valueOf( getOtherIDComputableValue().get() );
    }

    @Memoize
    int getOtherID()
    {
      observeADependency();
      return _otherID;
    }

    void setOtherID( @SuppressWarnings( "SameParameterValue" ) final int otherID )
    {
      _otherID = otherID;
    }
  }

  @Test
  public void ref()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ComputableValueRefTest_Arez_TestComponent();
    component.setOtherID( 1 );

    final Integer valueAsInt = context.action( component::getOtherID );
    final String valueAsString = context.action( component::getOther );

    assertEquals( valueAsInt.intValue(), 1 );
    assertEquals( valueAsString, "1" );
  }
}
