package arez.integration.dispose_on_deactivate;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ComputableValue;
import arez.Disposable;
import arez.Observer;
import arez.SchedulerLock;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MultipleDeactivationsDisposeOnDeactivateIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final AtomicInteger counter = new AtomicInteger();
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final Model1 model = Model1.create();

        assertFalse(Disposable.isDisposed(model));

        final Observer observer = observer(() -> ComponentObservable.observe(model));

        assertFalse(Disposable.isDisposed(observer));

        final SchedulerLock schedulerLock = Arez.context().pauseScheduler();

        // This will trigger scheduleDispose
        Disposable.dispose(observer);

        final ComputableValue<Integer> computableValue = Arez.context().computable(() -> {
            ComponentObservable.observe(model);
            return counter.incrementAndGet();
        });

        // This will re-observe model and re-activate and then deactivate which will trigger another scheduleDispose
        safeAction(computableValue::get);

        assertFalse(Disposable.isDisposed(computableValue));

        // Will trigger another scheduleDispose here
        Disposable.dispose(computableValue);

        schedulerLock.dispose();

        assertTrue(Disposable.isDisposed(model));
        assertTrue(Disposable.isDisposed(observer));
        assertTrue(Disposable.isDisposed(computableValue));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Model1 {
        static @Nullable Model1 create() {
            return new MultipleDeactivationsDisposeOnDeactivateIntegrationTest_Arez_Model1();
        }
    }
}
