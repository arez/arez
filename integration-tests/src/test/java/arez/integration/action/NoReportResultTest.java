package arez.integration.action;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "unused", "SameParameterValue" } )
public final class NoReportResultTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Action( reportResult = false )
    int myAction()
    {
      getTime();
      return 23;
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    final MyComponent component = new NoReportResultTest_Arez_MyComponent();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    component.myAction();

    recorder.assertEventCount( 4 );
    recorder.assertNextEvent( ActionStartEvent.class );
    recorder.assertNextEvent( TransactionStartEvent.class );
    recorder.assertNextEvent( TransactionCompleteEvent.class );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertNull( a.getResult() ) );
  }
}
