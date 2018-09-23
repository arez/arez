package arez.integration.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentDependencyFieldIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model1a = Model1.create();
    final Model1 model1b = Model1.create();
    final Model2 model2a = Model2.create( model1a );
    final Model2 model2b = Model2.create( model1b );

    assertFalse( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertFalse( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model2a );

    assertFalse( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model1a );

    assertTrue( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model1b );

    assertTrue( Disposable.isDisposed( model1a ) );
    assertTrue( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertTrue( Disposable.isDisposed( model2b ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model1
  {
    static Model1 create()
    {
      return new ComponentDependencyFieldIntegrationTest_Arez_Model1();
    }
  }

  @ArezComponent
  static abstract class Model2
  {
    @ComponentDependency( action = ComponentDependency.Action.CASCADE )
    final Model1 _reference;

    static Model2 create( final Model1 reference )
    {
      return new ComponentDependencyFieldIntegrationTest_Arez_Model2( reference );
    }

    Model2( final Model1 reference )
    {
      _reference = reference;
    }
  }
}
