package arez.integration.action;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionSkippedEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class SkipIfDisposedIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    int invokeCount;

    @Action( skipIfDisposed = true )
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @Test
  public void scenario()
  {
    final TestComponent component = new SkipIfDisposedIntegrationTest_Arez_TestComponent();
    Disposable.dispose( component );

    assertTrue( Disposable.isDisposed( component ) );

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( recorder );

    component.myAction( 123 );

    assertEquals( component.invokeCount, 0 );

    recorder.assertEventCount( 1 );
    recorder.assertNextEvent( ActionSkippedEvent.class, e -> {
      assertFalse( e.isTracked() );
      assertEquals( e.getParameters().length, 1 );
      assertEquals( e.getParameters()[ 0 ], 123 );
    } );
  }
}
