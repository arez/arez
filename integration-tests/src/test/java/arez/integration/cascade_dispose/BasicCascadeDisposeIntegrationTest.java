package arez.integration.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BasicCascadeDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final Model2 model2 = Model2.create();
    final Model1 model1 = Model1.create( model2 );

    assertEquals( Disposable.isDisposed( model1 ), false );
    assertEquals( Disposable.isDisposed( model2 ), false );

    Disposable.dispose( model1 );

    assertEquals( Disposable.isDisposed( model1 ), true );
    assertEquals( Disposable.isDisposed( model2 ), true );
  }

  @ArezComponent
  static abstract class Model1
  {
    @CascadeDispose
    Model2 _model2;

    @Nonnull
    static Model1 create( @Nonnull final Model2 name )
    {
      return new BasicCascadeDisposeIntegrationTest_Arez_Model1( name );
    }

    public Model1( @Nonnull final Model2 model2 )
    {
      _model2 = model2;
    }
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    @Nonnull
    static Model2 create()
    {
      return new BasicCascadeDisposeIntegrationTest_Arez_Model2();
    }
  }
}
