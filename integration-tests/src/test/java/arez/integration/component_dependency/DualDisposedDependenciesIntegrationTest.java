package arez.integration.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Arez:UnmanagedComponentReference" )
public final class DualDisposedDependenciesIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model0 model0a = Model0.create( "Model1A" );
    final Model0 model0b = Model0.create( "Model1B" );
    final Model1 model1a = Model1.create( "Model1A", model0a );
    final Model1 model1b = Model1.create( "Model1B", model0b );
    final Model2 model2a = Model2.create( model1a, "Model2A", model0a );
    final Model2 model2b = Model2.create( model1b, "Model2B", model0b );

    assertFalse( Disposable.isDisposed( model0a ) );
    assertFalse( Disposable.isDisposed( model0b ) );
    assertFalse( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertFalse( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model2a );

    assertFalse( Disposable.isDisposed( model0a ) );
    assertFalse( Disposable.isDisposed( model0b ) );
    assertFalse( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model0a );

    assertTrue( Disposable.isDisposed( model0a ) );
    assertFalse( Disposable.isDisposed( model0b ) );
    assertTrue( Disposable.isDisposed( model1a ) );
    assertFalse( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertFalse( Disposable.isDisposed( model2b ) );

    Disposable.dispose( model0b );

    assertTrue( Disposable.isDisposed( model0a ) );
    assertTrue( Disposable.isDisposed( model0b ) );
    assertTrue( Disposable.isDisposed( model1a ) );
    assertTrue( Disposable.isDisposed( model1b ) );
    assertTrue( Disposable.isDisposed( model2a ) );
    assertTrue( Disposable.isDisposed( model2b ) );

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

    @ComponentDependency
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

    @ComponentDependency
    final Model1 getReference()
    {
      return _reference;
    }

    @ComponentDependency
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
