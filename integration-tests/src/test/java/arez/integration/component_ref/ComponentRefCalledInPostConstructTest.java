package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentRefCalledInPostConstructTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        @PostConstruct
        void postConstruct() {
            getComponent();
        }

        @SuppressWarnings("UnusedReturnValue")
        @ComponentRef
        abstract @Nullable Component getComponent();
    }

    @Test
    public void scenario() {
        assertInvariant(
                ComponentRefCalledInPostConstructTest_Arez_TestComponent::new,
                "Method named 'getComponent' invoked on incomplete component named"
                        + " 'arez_integration_component_ref_ComponentRefCalledInPostConstructTest_TestComponent.1'");
    }
}
