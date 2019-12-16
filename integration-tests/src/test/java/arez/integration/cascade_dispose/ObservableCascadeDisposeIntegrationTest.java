package arez.integration.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableCascadeDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario1()
  {
    final Model2 model2 = Model2.create();
    final Model1 model1 = Model1.create( model2 );

    assertFalse( Disposable.isDisposed( model1 ) );
    assertFalse( Disposable.isDisposed( model2 ) );

    Disposable.dispose( model1 );

    assertTrue( Disposable.isDisposed( model1 ) );
    assertTrue( Disposable.isDisposed( model2 ) );
  }

  @Test
  public void scenario2()
  {
    final Model2 model2a = Model2.create();
    final Model2 model2b = Model2.create();
    final Model1 model1 = Model1.create( model2a );

    assertFalse( Disposable.isDisposed( model1 ) );
    assertFalse( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    model1.setModel2( model2b );

    Disposable.dispose( model1 );

    assertTrue( Disposable.isDisposed( model1 ) );
    assertFalse( Disposable.isDisposed( model2a ) );
    assertTrue( Disposable.isDisposed( model2b ) );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    static Model1 create( @Nonnull final Model2 name )
    {
      return new ObservableCascadeDisposeIntegrationTest_Arez_Model1( name );
    }

    @SuppressWarnings( "unused" )
    @CascadeDispose
    @Nonnull
    abstract Model2 getModel2();

    @Observable( writeOutsideTransaction = Feature.ENABLE )
    abstract void setModel2( @Nonnull Model2 model2 );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    @Nonnull
    static Model2 create()
    {
      return new ObservableCascadeDisposeIntegrationTest_Arez_Model2();
    }
  }
}
