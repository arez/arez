package arez.integration.observe;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
public final class ObserveNonArezDependenciesTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class TestComponent1 {
        int _renderCallCount;

        @Observe(depType = DepType.AREZ_OR_EXTERNAL)
        void render() {
            getValue();
            _renderCallCount++;
        }

        @ObserverRef
        abstract @Nullable Observer getRenderObserver();

        @SuppressWarnings("UnusedReturnValue")
        @Observable
        abstract long getValue();

        abstract void setValue(long value);
    }

    @Test
    public void scenario() {
        final TestComponent1 component = new ObserveNonArezDependenciesTest_Arez_TestComponent1();

        assertEquals(component._renderCallCount, 1);

        // reportStale should result in re-render invocation
        Arez.context().safeAction(() -> component.getRenderObserver().reportStale());

        assertEquals(component._renderCallCount, 2);

        // Trigger a dependency change, mark observer as dirty
        safeAction(() -> component.setValue(ValueUtil.randomLong()));

        assertEquals(component._renderCallCount, 3);

        // reportStale and dependency change should result in single re-render invocation
        Arez.context().safeAction(() -> {
            component.setValue(ValueUtil.randomLong());
            component.getRenderObserver().reportStale();
        });

        assertEquals(component._renderCallCount, 4);
    }
}
