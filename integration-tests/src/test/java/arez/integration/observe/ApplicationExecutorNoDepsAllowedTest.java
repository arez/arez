package arez.integration.observe;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObserverInfo;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ApplicationExecutorNoDepsAllowedTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class TestComponent1 {
        int _renderCallCount;

        @Observe(executor = Executor.EXTERNAL, depType = DepType.AREZ_OR_NONE)
        void render() {
            _renderCallCount++;
        }

        @OnDepsChange
        void onRenderDepsChange() {}

        @ObserverRef
        abstract @Nullable Observer getRenderObserver();
    }

    @Test
    public void scenario() {
        final TestComponent1 component = new ApplicationExecutorNoDepsAllowedTest_Arez_TestComponent1();

        assertEquals(component._renderCallCount, 0);

        // Should generate no exception even though there is no observer
        component.render();

        assertEquals(component._renderCallCount, 1);

        final ObserverInfo info = Arez.context().getSpy().asObserverInfo(component.getRenderObserver());
        assertEquals(info.getDependencies().size(), 0);
    }
}
