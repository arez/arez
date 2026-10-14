package arez.integration.observable_component;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableComponentIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void disposeNotifiesComponentObservable() {
        final Model model = Model.create();
        final AtomicInteger callCount = new AtomicInteger();
        Arez.context().observer(() -> {
            observeADependency();
            ComponentObservable.observe(model);
            callCount.incrementAndGet();
        });
        assertEquals(callCount.get(), 1);

        Disposable.dispose(model);

        assertEquals(callCount.get(), 2);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true, observable = Feature.ENABLE)
    abstract static class Model {

        static Model create() {
            return new ObservableComponentIntegrationTest_Arez_Model();
        }
    }
}
