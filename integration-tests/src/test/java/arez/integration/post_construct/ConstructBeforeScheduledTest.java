package arez.integration.post_construct;

import static org.testng.Assert.*;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class ConstructBeforeScheduledTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class TestComponent {
        boolean _postConstructCalled;
        boolean _observerCalledBeforePostConstructCalled;
        boolean _observerCalled;

        @PostConstruct
        void postConstruct() {
            _postConstructCalled = true;
        }

        @Observe
        void observer() {
            // Observe something so it is valid observed
            observeADependency();

            _observerCalled = true;
            if (!_postConstructCalled) {
                _observerCalledBeforePostConstructCalled = true;
            }
        }
    }

    @Test
    public void observerAndPostConstructSequencing() {
        final TestComponent component = new ConstructBeforeScheduledTest_Arez_TestComponent();

        assertTrue(component._observerCalled);
        assertTrue(component._postConstructCalled);
        assertFalse(component._observerCalledBeforePostConstructCalled);
    }
}
