package arez.integration.observe;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TrackNoReportResultTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class Model
  {
    @Observe( executor = Executor.APPLICATION, reportResult = false )
    public int render()
    {
      getTime();
      return 23;
    }

    @OnDepsChange
    final void onRenderDepsChange()
    {
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    final Model component = new TrackNoReportResultTest_Arez_Model();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    final int result = component.render();
    assertEquals( result, 23 );

    recorder.assertEventCount( 4 );
    recorder.assertNextEvent( ActionStartedEvent.class );
    recorder.assertNextEvent( TransactionStartedEvent.class );
    recorder.assertNextEvent( TransactionCompletedEvent.class );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertNull( a.getResult() ) );
  }
}
