package arez.integration.auto_observe;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class AutoObserveObservableIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var child1 = Child.create();
        final var owner = Owner.create();

        safeAction(() -> owner.setChild(child1));

        assertFalse(Disposable.isDisposed(child1));

        safeAction(() -> owner.setChild(null));

        assertTrue(Disposable.isDisposed(child1));

        final var child2 = Child.create();

        safeAction(() -> owner.setChild(child2));

        assertFalse(Disposable.isDisposed(child2));

        Disposable.dispose(owner);

        assertTrue(Disposable.isDisposed(child2));
    }

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Child {

        static Child create() {
            return new AutoObserveObservableIntegrationTest_Arez_Child();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Owner {

        static Owner create() {
            return new AutoObserveObservableIntegrationTest_Arez_Owner();
        }

        @AutoObserve
        @Observable
        @Nullable
        abstract Child getChild();

        abstract void setChild(@Nullable Child child);
    }
}
