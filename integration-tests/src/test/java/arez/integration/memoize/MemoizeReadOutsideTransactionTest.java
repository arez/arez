package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
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

public class MemoizeReadOutsideTransactionTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    element.getMemoized();

    recorder.assertEventCount( 7 );
    recorder.assertNextEvent( TransactionStartEvent.class );

    recorder.assertNextEvent( ComputeStartEvent.class );
    recorder.assertNextEvent( TransactionStartEvent.class );

    recorder.assertNextEvent( ObservableValueChangeEvent.class );

    recorder.assertNextEvent( TransactionCompleteEvent.class );
    recorder.assertNextEvent( ComputeCompleteEvent.class );

    recorder.assertNextEvent( TransactionCompleteEvent.class );
  }

  @ArezComponent
  static abstract class Element
  {
    @Nonnull
    static Element create()
    {
      return new MemoizeReadOutsideTransactionTest_Arez_Element();
    }

    @Memoize( readOutsideTransaction = true, depType = DepType.AREZ_OR_NONE )
    @Nonnull
    String getMemoized()
    {
      return "";
    }
  }
}
