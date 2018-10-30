package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedNoReportResultTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    safeAction( element::getComputed );

    recorder.assertEventCount( 9 );
    recorder.assertNextEvent( ActionStartedEvent.class );
    recorder.assertNextEvent( TransactionStartedEvent.class );

    recorder.assertNextEvent( ComputeStartedEvent.class );
    recorder.assertNextEvent( TransactionStartedEvent.class );

    recorder.assertNextEvent( ObservableValueChangedEvent.class );

    recorder.assertNextEvent( TransactionCompletedEvent.class );
    recorder.assertNextEvent( ComputeCompletedEvent.class, a -> assertNull( a.getResult() ) );

    recorder.assertNextEvent( TransactionCompletedEvent.class );
    recorder.assertNextEvent( ActionCompletedEvent.class );
  }

  @ArezComponent
  static abstract class Element
  {
    @Nonnull
    static Element create()
    {
      return new ComputedNoReportResultTest_Arez_Element();
    }

    @Computed( reportResult = false, depType = DepType.AREZ_OR_NONE )
    @Nonnull
    String getComputed()
    {
      return "";
    }
  }
}
