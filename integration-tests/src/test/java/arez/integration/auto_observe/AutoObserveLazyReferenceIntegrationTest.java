package arez.integration.auto_observe;

import static org.testng.Assert.*;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Feature;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class AutoObserveLazyReferenceIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final TypeBasedLocator locator = new TypeBasedLocator();
        Arez.context().registerLocator(locator);

        final AtomicInteger findCallCount = new AtomicInteger();
        final HashMap<Object, Child> entities = new HashMap<>();
        locator.registerLookup(Child.class, id -> {
            findCallCount.incrementAndGet();
            return entities.get(id);
        });

        final Child child = Child.create();
        final Object childId = Objects.requireNonNull(Identifiable.getArezId(child));
        entities.put(childId, child);

        assertEquals(findCallCount.get(), 0);

        final Owner owner = Owner.create(childId);

        assertEquals(findCallCount.get(), 1);
        assertFalse(Disposable.isDisposed(child));

        Disposable.dispose(owner);

        assertTrue(Disposable.isDisposed(child));
    }

    @NullMarked
    @ArezComponent
    abstract static class Owner {
        @Nullable
        private Object _childId;

        static Owner create(@Nullable final Object childId) {
            return new AutoObserveLazyReferenceIntegrationTest_Arez_Owner(childId);
        }

        Owner(@Nullable final Object childId) {
            _childId = childId;
        }

        @AutoObserve
        @Reference(load = LinkType.LAZY)
        @Nullable
        abstract Child getChild();

        @ReferenceId
        @Observable
        @Nullable
        Object getChildId() {
            return _childId;
        }

        void setChildId(@Nullable final Object childId) {
            _childId = childId;
        }
    }

    @NullMarked
    @ArezComponent(allowEmpty = true, disposeOnDeactivate = true, requireId = Feature.ENABLE)
    abstract static class Child {

        static Child create() {
            return new AutoObserveLazyReferenceIntegrationTest_Arez_Child();
        }
    }
}
