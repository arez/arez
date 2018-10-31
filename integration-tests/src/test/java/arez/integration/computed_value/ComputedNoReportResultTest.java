package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
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
    recorder.assertNextEvent( ActionStartEvent.class );
    recorder.assertNextEvent( TransactionStartEvent.class );

    recorder.assertNextEvent( ComputeStartEvent.class );
    recorder.assertNextEvent( TransactionStartEvent.class );

    recorder.assertNextEvent( ObservableValueChangeEvent.class );

    recorder.assertNextEvent( TransactionCompleteEvent.class );
    recorder.assertNextEvent( ComputeCompleteEvent.class, a -> assertNull( a.getResult() ) );

    recorder.assertNextEvent( TransactionCompleteEvent.class );
    recorder.assertNextEvent( ActionCompleteEvent.class );
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
