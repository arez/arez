package arez.integration.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SetNullOnDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model1a = Model1.create( "Model1A" );
    final Model1 model1b = Model1.create( "Model1B" );
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

    assertNotNull( safeAction( model2b::getReference ) );
    Disposable.dispose( model1b );

    assertTrue( Disposable.isDisposed( model1a ) );
    assertTrue( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    assertNull( safeAction( model2b::getReference ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent
  static abstract class Model1
  {
    private String _name;

    static Model1 create( final String name )
    {
      return new SetNullOnDisposeIntegrationTest_Arez_Model1( name );
    }

    Model1( final String name )
    {
      _name = name;
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model2
  {
    private Model1 _reference;

    static Model2 create( final Model1 reference )
    {
      return new SetNullOnDisposeIntegrationTest_Arez_Model2( reference );
    }

    Model2( final Model1 reference )
    {
      _reference = reference;
    }

    @Observable
    @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
    Model1 getReference()
    {
      return _reference;
    }

    void setReference( final Model1 reference )
    {
      _reference = reference;
    }
  }
}
