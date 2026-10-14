package arez.integration.context_ref;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ContextRefTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class TestComponent {
        @ContextRef
        abstract @Nullable ArezContext getContext();
    }

    @Test
    public void contextRef() {
        final ArezContext context = Arez.context();

        final TestComponent component = new ContextRefTest_Arez_TestComponent();
        assertEquals(component.getContext(), context);
    }
}
