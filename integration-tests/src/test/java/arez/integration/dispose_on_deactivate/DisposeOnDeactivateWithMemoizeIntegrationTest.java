package arez.integration.dispose_on_deactivate;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.Priority;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Arez:UnmanagedComponentReference" )
public final class DisposeOnDeactivateWithMemoizeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    /*
     * This scenario has model 2 and model 3 watching model1. Model 3 is observing
     * model 2 but will un-observe model 2 when model 1 changes. Model 2 is
     * disposeOnDeactivate=true and will dispose when this occurs. This test ensure
     * that the priority on the reaction that disposes model 2 is higher than the
     * @Memoize on model 2 thus ensuring that model 2 will not schedule @Memoize
     * when it will ultimately be disposed.
     */

    final Model1 model1 = Model1.create();
    final Model2 model2 = Model2.create( model1 );
    final Model3 model3 = Model3.create( model1, model2 );

    assertEquals( model2._callCount.get(), 1 );
    assertEquals( model3._callCount.get(), 1 );

    assertFalse( Disposable.isDisposed( model2 ) );
    assertFalse( Disposable.isDisposed( model3 ) );

    context.safeAction( "MyAction", () -> model1.setName( "Hello" ) );

    assertEquals( model2._callCount.get(), 1 );
    assertEquals( model3._callCount.get(), 3 );

    assertTrue( Disposable.isDisposed( model2 ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent
  static abstract class Model1
  {
    static Model1 create()
    {
      return new DisposeOnDeactivateWithMemoizeIntegrationTest_Arez_Model1();
    }

    @Observable
    abstract String getName();

    abstract void setName( String name );
  }

  @ArezComponent( disposeOnDeactivate = true )
  static abstract class Model2
  {
    static Model2 create( @Nonnull final Model1 model1 )
    {
      return new DisposeOnDeactivateWithMemoizeIntegrationTest_Arez_Model2( model1 );
    }

    private final Model1 _model1;
    final AtomicInteger _callCount = new AtomicInteger();

    Model2( @Nonnull final Model1 model1 )
    {
      _model1 = model1;
    }

    @Memoize( keepAlive = true )
    public int someValue()
    {
      _callCount.incrementAndGet();
      _model1.getName();
      return 21;
    }
  }

  @ArezComponent
  static abstract class Model3
  {
    static Model3 create( @Nonnull final Model1 model1, @Nonnull final Model2 model2 )
    {
      return new DisposeOnDeactivateWithMemoizeIntegrationTest_Arez_Model3( model1, model2 );
    }

    private final Model1 _model1;
    private final Model2 _model2;
    final AtomicInteger _callCount = new AtomicInteger();

    Model3( @Nonnull final Model1 model1, @Nonnull final Model2 model2 )
    {
      _model1 = model1;
      _model2 = model2;
    }

    @Observe( mutation = true, priority = Priority.HIGH )
    void myAutorun()
    {
      _callCount.incrementAndGet();
      final String name = _model1.getName();
      if ( null == name )
      {
        ComponentObservable.observe( _model2 );
      }
      else if ( !"Blah".equals( name ) )
      {
        _model1.setName( "Blah" );
      }
    }
  }
}
