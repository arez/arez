package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeReadOutsideTransactionTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final ArezContext context = Arez.context();

        final var element = Element.create();

        final var recorder = new TestSpyEventHandler();
        context.getSpy().addSpyEventHandler(recorder);

        element.getMemoized();

        recorder.assertEventCount(7);
        recorder.assertNextEvent(TransactionStartEvent.class);

        recorder.assertNextEvent(ComputeStartEvent.class);
        recorder.assertNextEvent(TransactionStartEvent.class);

        recorder.assertNextEvent(ObservableValueChangeEvent.class);

        recorder.assertNextEvent(TransactionCompleteEvent.class);
        recorder.assertNextEvent(ComputeCompleteEvent.class);

        recorder.assertNextEvent(TransactionCompleteEvent.class);
    }

    @NullMarked
    @ArezComponent
    abstract static class Element {

        static Element create() {
            return new MemoizeReadOutsideTransactionTest_Arez_Element();
        }

        @Memoize(readOutsideTransaction = Feature.ENABLE, depType = DepType.AREZ_OR_NONE)
        String getMemoized() {
            return "";
        }
    }
}
