package arez.integration.dependency;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.integration.AbstractIntegrationTest;
import arez.integration.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SetNullOnDisposeIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model1a = Model1.create( "Model1A" );
    final Model1 model1b = Model1.create( "Model1B" );
    final Model2 model2a = Model2.create( model1a );
    final Model2 model2b = Model2.create( model1b );

    assertEquals( Disposable.isDisposed( model1a ), false );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), false );
    assertEquals( Disposable.isDisposed( model2b ), false );

    Disposable.dispose( model2a );

    assertEquals( Disposable.isDisposed( model1a ), false );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), false );

    Disposable.dispose( model1a );

    assertEquals( Disposable.isDisposed( model1a ), true );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), false );

    assertNotNull( context.safeAction( model2b::getReference ) );
    Disposable.dispose( model1b );

    assertEquals( Disposable.isDisposed( model1a ), true );
    assertEquals( Disposable.isDisposed( model1b ), true );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), false );

    assertNull( context.safeAction( model2b::getReference ) );

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

    @Dependency( action = Dependency.Action.SET_NULL )
    Model1 getReference()
    {
      return _reference;
    }

    @Observable
    void setReference( final Model1 reference )
    {
      _reference = reference;
    }
  }
}
