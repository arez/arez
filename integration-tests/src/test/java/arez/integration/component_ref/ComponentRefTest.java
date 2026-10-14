package arez.integration.component_ref;

import static org.testng.Assert.*;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentRefTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        @ComponentRef
        abstract @Nullable Component getComponent();
    }

    @Test
    public void componentRef() {
        final TestComponent instance = new ComponentRefTest_Arez_TestComponent();
        final Component component = instance.getComponent();
        assertNotNull(component);
        assert null != component;
        assertEquals(component.getType(), "arez_integration_component_ref_ComponentRefTest_TestComponent");
    }
}
