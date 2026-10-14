package arez.integration.interface_component;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class InterfaceComponentTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    interface TestComponent {
        @Observable
        void setMyVar(int value);

        int getMyVar();

        @Memoize
        default boolean isOver2() {
            return getMyVar() > 2;
        }
    }

    @Test
    public void scenario() {
        final ArezContext context = Arez.context();

        final TestComponent component = new InterfaceComponentTest_Arez_TestComponent();
        context.safeAction(() -> component.setMyVar(1));

        assertEquals(context.safeAction(component::getMyVar).intValue(), 1);
        assertFalse(context.safeAction(component::isOver2));

        context.safeAction(() -> component.setMyVar(33));

        assertEquals(context.safeAction(component::getMyVar).intValue(), 33);
        assertTrue(context.safeAction(component::isOver2));
    }
}
