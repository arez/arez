package arez.integration.observe;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class ApplicationExecutorNoDepsDisallowedTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class TestComponent1 {
        int _renderCallCount;

        @Observe(executor = Executor.EXTERNAL, depType = DepType.AREZ)
        void render() {
            _renderCallCount++;
        }

        @OnDepsChange
        void onRenderDepsChange() {}
    }

    @Test
    public void scenario() {
        final TestComponent1 component = new ApplicationExecutorNoDepsDisallowedTest_Arez_TestComponent1();

        assertEquals(component._renderCallCount, 0);

        assertInvariant(
                component::render,
                "Arez-0118: Observer named"
                    + " 'arez_integration_observe_ApplicationExecutorNoDepsDisallowedTest_TestComponent1.1.render'"
                    + " completed observed function (executed by application) but is not observing any properties.");

        assertEquals(component._renderCallCount, 1);
    }
}
