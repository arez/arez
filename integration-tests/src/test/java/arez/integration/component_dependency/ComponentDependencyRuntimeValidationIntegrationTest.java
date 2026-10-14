package arez.integration.component_dependency;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentDependencyRuntimeValidationIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final var model1a1 = Model1.create();
        final var model2a = Model2.create(model1a1);

        assertFalse(Disposable.isDisposed(model1a1));
        assertFalse(Disposable.isDisposed(model2a));

        Disposable.dispose(model1a1);

        assertTrue(Disposable.isDisposed(model1a1));
        assertTrue(Disposable.isDisposed(model2a));
    }

    @Test
    public void scenario_withBadDependency() {
        final var value = new BadType();
        final IllegalStateException exception = expectThrows(IllegalStateException.class, () -> Model2.create(value));
        assertEquals(
                exception.getMessage(),
                "Arez-0178: Object passed to asDisposeNotifier does not implement " + "DisposeNotifier. Object: "
                        + value);
    }

    @NullMarked
    interface MyInterface {}

    @NullMarked
    private static class BadType implements MyInterface {}

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model1 implements MyInterface {

        static Model1 create() {
            return new ComponentDependencyRuntimeValidationIntegrationTest_Arez_Model1();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model2 {
        @ComponentDependency(validateTypeAtRuntime = true)
        final @Nullable MyInterface _reference1;

        static Model2 create(final @Nullable MyInterface reference1) {
            return new ComponentDependencyRuntimeValidationIntegrationTest_Arez_Model2(reference1);
        }

        Model2(final @Nullable MyInterface reference1) {
            _reference1 = reference1;
        }
    }
}
