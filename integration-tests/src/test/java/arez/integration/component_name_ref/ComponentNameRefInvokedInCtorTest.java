package arez.integration.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentNameRefInvokedInCtorTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        TestComponent() {
            getName();
        }

        @SuppressWarnings("UnusedReturnValue")
        @ComponentNameRef
        abstract @Nullable String getName();
    }

    @Test
    public void scenario() {
        assertInvariant(
                ComponentNameRefInvokedInCtorTest_Arez_TestComponent::new,
                "Method named 'getName' invoked on uninitialized component of type"
                        + " 'arez_integration_component_name_ref_ComponentNameRefInvokedInCtorTest_TestComponent'");
    }
}
