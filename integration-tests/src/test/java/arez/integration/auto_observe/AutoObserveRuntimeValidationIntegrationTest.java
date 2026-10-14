package arez.integration.auto_observe;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.AutoObserve;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class AutoObserveRuntimeValidationIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var child = Child.create();
        final var owner = Owner.create(child);

        assertFalse(Disposable.isDisposed(child));

        Disposable.dispose(owner);

        assertTrue(Disposable.isDisposed(child));
    }

    @Test
    public void scenario_withBadDependency() {
        captureObserverErrors();
        final var value = new BadType();
        assertInvariant(
                () -> Owner.create(value),
                "Field annotated with @AutoObserve( validateTypeAtRuntime = true ) references a "
                        + "non-null value that does not implement ComponentObservable. Value: " + value);
    }

    @Test
    public void scenario_withNullDependency() {
        captureObserverErrors();
        final var owner = Owner.create(null);

        assertNotNull(owner);
        assert null != owner;
        assertEquals(getObserverErrors().size(), 0);

        Disposable.dispose(owner);
    }

    @NullMarked
    @ArezComponentLike
    interface MyType {}

    @NullMarked
    private static final class BadType implements MyType {}

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Child implements MyType {

        static Child create() {
            return new AutoObserveRuntimeValidationIntegrationTest_Arez_Child();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Owner {
        @AutoObserve(validateTypeAtRuntime = true)
        @Nullable
        final MyType _child;

        static Owner create(@Nullable final MyType child) {
            return new AutoObserveRuntimeValidationIntegrationTest_Arez_Owner(child);
        }

        Owner(@Nullable final MyType child) {
            _child = child;
        }
    }
}
