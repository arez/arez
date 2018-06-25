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

public class DualDisposedDependenciesIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model0 model0a = Model0.create( "Model1A" );
    final Model0 model0b = Model0.create( "Model1B" );
    final Model1 model1a = Model1.create( "Model1A", model0a );
    final Model1 model1b = Model1.create( "Model1B", model0b );
    final Model2 model2a = Model2.create( model1a, "Model2A", model0a );
    final Model2 model2b = Model2.create( model1b, "Model2B", model0b );

    assertEquals( Disposable.isDisposed( model0a ), false );
    assertEquals( Disposable.isDisposed( model0b ), false );
    assertEquals( Disposable.isDisposed( model1a ), false );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), false );
    assertEquals( Disposable.isDisposed( model2b ), false );

    Disposable.dispose( model2a );

    assertEquals( Disposable.isDisposed( model0a ), false );
    assertEquals( Disposable.isDisposed( model0b ), false );
    assertEquals( Disposable.isDisposed( model1a ), false );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), false );

    Disposable.dispose( model0a );

    assertEquals( Disposable.isDisposed( model0a ), true );
    assertEquals( Disposable.isDisposed( model0b ), false );
    assertEquals( Disposable.isDisposed( model1a ), true );
    assertEquals( Disposable.isDisposed( model1b ), false );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), false );

    Disposable.dispose( model0b );

    assertEquals( Disposable.isDisposed( model0a ), true );
    assertEquals( Disposable.isDisposed( model0b ), true );
    assertEquals( Disposable.isDisposed( model1a ), true );
    assertEquals( Disposable.isDisposed( model1b ), true );
    assertEquals( Disposable.isDisposed( model2a ), true );
    assertEquals( Disposable.isDisposed( model2b ), true );

    assertMatchesFixture( recorder );
  }

  @ArezComponent
  static abstract class Model0
  {
    private String _name;

    static Model0 create( final String name )
    {
      return new DualDisposedDependenciesIntegrationTest_Arez_Model0( name );
    }

    Model0( final String name )
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
  static abstract class Model1
  {
    private String _name;
    private Model0 _leaf;

    static Model1 create( final String name, final Model0 leaf )
    {
      return new DualDisposedDependenciesIntegrationTest_Arez_Model1( name, leaf );
    }

    Model1( final String name, final Model0 leaf )
    {
      _name = name;
      _leaf = leaf;
    }

    @Dependency
    @Observable
    Model0 getLeaf()
    {
      return _leaf;
    }

    @Observable
    void setLeaf( final Model0 leaf )
    {
      _leaf = leaf;
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
    private final Model0 _leaf;
    private final Model1 _reference;
    private String _name;

    static Model2 create( final Model1 reference, final String name, final Model0 leaf )
    {
      return new DualDisposedDependenciesIntegrationTest_Arez_Model2( reference, name, leaf );
    }

    Model2( final Model1 reference, final String name, final Model0 leaf )
    {
      _reference = reference;
      _name = name;
      _leaf = leaf;
    }

    @Dependency
    final Model1 getReference()
    {
      return _reference;
    }

    @Dependency
    final Model0 getLeaf()
    {
      return _leaf;
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
