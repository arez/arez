package arez.integration.component_dependency;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class SetNullOnDisposeIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final Model1 model1a = Model1.create("Model1A");
        final Model1 model1b = Model1.create("Model1B");
        final Model2 model2a = Model2.create(model1a);
        final Model2 model2b = Model2.create(model1b);

        assertFalse(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertFalse(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        Disposable.dispose(model2a);

        assertFalse(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        Disposable.dispose(model1a);

        assertTrue(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        assertNotNull(safeAction(model2b::getReference));
        Disposable.dispose(model1b);

        assertTrue(Disposable.isDisposed(model1a));
        assertTrue(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        assertNull(safeAction(model2b::getReference));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {
        private @Nullable String _name;

        static @Nullable Model1 create(final @Nullable String name) {
            return new SetNullOnDisposeIntegrationTest_Arez_Model1(name);
        }

        Model1(final @Nullable String name) {
            _name = name;
        }

        @Observable
        @Nullable
        String getName() {
            return _name;
        }

        void setName(@Nullable String name) {
            _name = name;
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model2 {
        @SuppressWarnings("Arez:UnmanagedComponentReference")
        private @Nullable Model1 _reference;

        static @Nullable Model2 create(final @Nullable Model1 reference) {
            return new SetNullOnDisposeIntegrationTest_Arez_Model2(reference);
        }

        Model2(final @Nullable Model1 reference) {
            _reference = reference;
        }

        @Observable
        @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
        @Nullable
        Model1 getReference() {
            return _reference;
        }

        void setReference(final @Nullable Model1 reference) {
            _reference = reference;
        }
    }
}
