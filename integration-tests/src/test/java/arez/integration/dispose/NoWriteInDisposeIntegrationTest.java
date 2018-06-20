package arez.integration.dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NoWriteInDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final Model1 model1 = Model1.create( ValueUtil.randomString() );
    final Model2 model2 = Model2.create( model1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Disposable.dispose( model2 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0156: Transaction named 'Model2.0.dispose' attempted to change observable named 'Model1.0.name' but transaction mode is DISPOSE." );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    static Model1 create( @Nonnull final String name )
    {
      return new NoWriteInDisposeIntegrationTest_Arez_Model1( name );
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    @Nonnull
    private Model1 _other;

    @Nonnull
    static Model2 create( @Nonnull final Model1 other )
    {
      return new NoWriteInDisposeIntegrationTest_Arez_Model2( other );
    }

    Model2( @Nonnull final Model1 other )
    {
      _other = other;
    }

    @PreDispose
    final void preDispose()
    {
      _other.setName( "X" );
    }
  }
}
