package arez.integration.dependency;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CascadeOnDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model1a = Model1.create( "Model1A" );
    final Model1 model1b = Model1.create( "Model1B" );
    final Model2 model2a = Model2.create( model1a, "Model2A" );
    final Model2 model2b = Model2.create( model1b, "Model2B" );

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

    Disposable.dispose( model1b );

    assertEquals( Disposable.isDisposed( model1a ), true );
    assertEquals( Disposable.isDisposed( model1b ), true );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), true );

    assertMatchesFixture( recorder );
  }

  @ArezComponent
  static abstract class Model1
  {
    private String _name;

    static Model1 create( final String name )
    {
      return new CascadeOnDisposeIntegrationTest_Arez_Model1( name );
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
    private final Model1 _reference;
    private String _name;

    static Model2 create( final Model1 reference, final String name )
    {
      return new CascadeOnDisposeIntegrationTest_Arez_Model2( reference, name );
    }

    Model2( final Model1 reference, final String name )
    {
      _reference = reference;
      _name = name;
    }

    @Dependency( action = Dependency.Action.CASCADE )
    Model1 getReference()
    {
      return _reference;
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull String name )
    {
      _name = name;
    }
  }
}
