package arez.integration.auto_observe;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.Test;

@NullMarked
public final class AutoObserveFieldIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final Child child = Child.create();
        final Owner owner = Owner.create(child);

        assertFalse(Disposable.isDisposed(child));
        assertFalse(Disposable.isDisposed(owner));

        Disposable.dispose(owner);

        assertTrue(Disposable.isDisposed(owner));
        assertTrue(Disposable.isDisposed(child));
    }

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Child {

        static Child create() {
            return new AutoObserveFieldIntegrationTest_Arez_Child();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Owner {
        @AutoObserve
        final Child _child;

        static Owner create(final Child child) {
            return new AutoObserveFieldIntegrationTest_Arez_Owner(child);
        }

        Owner(final Child child) {
            _child = child;
        }
    }
}
