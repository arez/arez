package arez.integration.dispose_on_deactivate;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class DisposeOnDeactivateIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final var model = Model1.create();

        assertFalse(Disposable.isDisposed(model));

        final Observer observer = observer(() -> ComponentObservable.observe(model));

        assertFalse(Disposable.isDisposed(observer));

        Disposable.dispose(observer);

        assertTrue(Disposable.isDisposed(model));
        assertTrue(Disposable.isDisposed(observer));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Model1 {
        static Model1 create() {
            return new DisposeOnDeactivateIntegrationTest_Arez_Model1();
        }
    }
}
