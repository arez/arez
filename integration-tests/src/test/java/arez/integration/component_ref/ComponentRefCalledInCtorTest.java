package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentRefCalledInCtorTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        TestComponent() {
            getComponent();
        }

        @SuppressWarnings("UnusedReturnValue")
        @ComponentRef
        abstract @Nullable Component getComponent();
    }

    @Test
    public void scenario() {
        assertInvariant(
                ComponentRefCalledInCtorTest_Arez_TestComponent::new,
                "Method named 'getComponent' invoked on uninitialized component of type"
                        + " 'arez_integration_component_ref_ComponentRefCalledInCtorTest_TestComponent'");
    }
}
