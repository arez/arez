package arez.integration.observable;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NonnullObservableIntegrationTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void disallowNullInSetter()
    throws Throwable
  {
    final Model model = Model.create( ValueUtil.randomString() );
    safeAction( () -> assertThrows( AssertionError.class, () -> model.setLastName( null ) ) );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void disallowNullInInitializer()
    throws Throwable
  {
    assertThrows( NullPointerException.class, () -> Model.create( null ) );
  }

  @ArezComponent
  static abstract class Model
  {
    @Nonnull
    static Model create( final String lastName )
    {
      return new NonnullObservableIntegrationTest_Arez_Model( lastName );
    }

    @Observable
    @Nonnull
    abstract String getLastName();

    abstract void setLastName( @Nonnull String lastName );
  }
}
