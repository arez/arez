package arez.integration.component_ref;

import static org.testng.Assert.*;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public final class ComponentRefTest extends AbstractArezIntegrationTest {
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        @ComponentRef
        abstract Component getComponent();
    }

    @Test
    public void componentRef() {
        final TestComponent instance = new ComponentRefTest_Arez_TestComponent();
        final Component component = instance.getComponent();
        assertNotNull(component);
        assertEquals(component.getType(), "arez_integration_component_ref_ComponentRefTest_TestComponent");
    }
}
