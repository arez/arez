package arez;

import static org.testng.Assert.*;

import arez.spy.ActionStartEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import java.lang.reflect.AnnotatedArrayType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

public final class NullabilityContractTest extends AbstractTest {
    @Test
    public void actionArrayAndElementsHaveIndependentContracts() throws Exception {
        final var action =
                ArezContext.class.getMethod("safeAction", String.class, SafeFunction.class, int.class, Object[].class);
        final var parameters = (AnnotatedArrayType) action.getAnnotatedParameterTypes()[3];
        assertNotNull(parameters.getAnnotation(Nullable.class));
        assertNotNull(parameters.getAnnotatedGenericComponentType().getAnnotation(Nullable.class));

        final var event = ActionStartEvent.class.getConstructor(String.class, boolean.class, Object[].class);
        final var eventParameters = (AnnotatedArrayType) event.getAnnotatedParameterTypes()[2];
        assertNull(eventParameters.getAnnotation(Nullable.class));
        assertNotNull(eventParameters.getAnnotatedGenericComponentType().getAnnotation(Nullable.class));
        assertTrue(ActionStartEvent.class.getPackage().isAnnotationPresent(NullMarked.class));
    }

    @Test
    public void valueCallbacksRetainParametricNullness() {
        for (final var type :
                new Class<?>[] {Function.class, SafeFunction.class, PropertyAccessor.class, PropertyMutator.class}) {
            assertNotNull(type.getTypeParameters()[0].getAnnotatedBounds()[0].getAnnotation(Nullable.class));
            assertTrue(type.getPackage().isAnnotationPresent(NullMarked.class));
        }
        final SafeFunction<@Nullable String> nullableResult = () -> null;
        assertNull(Arez.context().safeAction("nullable", nullableResult, ActionFlags.NO_VERIFY_ACTION_REQUIRED, null));
        final @Nullable Object[] arguments = {null};
        assertNull(Arez.context()
                .safeAction("nullable", nullableResult, ActionFlags.NO_VERIFY_ACTION_REQUIRED, arguments));
        assertNull(new ActionStartEvent("nullable", false, arguments).getParameters()[0]);
    }
}
