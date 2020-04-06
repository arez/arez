package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NullableInitializerIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    safeAction( () -> assertNull( Model.create( null ).getName() ) );
    safeAction( () -> assertEquals( Model.create( "XXX" ).getName(), "XXX" ) );
  }

  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  static abstract class Model
  {
    @Nonnull
    static Model create( @Nullable final String name )
    {
      return new NullableInitializerIntegrationTest_Arez_Model( name );
    }

    @Observable( initializer = Feature.ENABLE )
    @Nullable
    abstract String getName();

    abstract void setName( @Nullable String name );
  }
}
