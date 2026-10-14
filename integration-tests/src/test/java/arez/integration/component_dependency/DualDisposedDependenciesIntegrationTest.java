package arez.integration.component_dependency;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
@SuppressWarnings("Arez:UnmanagedComponentReference")
public final class DualDisposedDependenciesIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final Model0 model0a = Model0.create("Model1A");
        final Model0 model0b = Model0.create("Model1B");
        final Model1 model1a = Model1.create("Model1A", model0a);
        final Model1 model1b = Model1.create("Model1B", model0b);
        final Model2 model2a = Model2.create(model1a, "Model2A", model0a);
        final Model2 model2b = Model2.create(model1b, "Model2B", model0b);

        assertFalse(Disposable.isDisposed(model0a));
        assertFalse(Disposable.isDisposed(model0b));
        assertFalse(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertFalse(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        Disposable.dispose(model2a);

        assertFalse(Disposable.isDisposed(model0a));
        assertFalse(Disposable.isDisposed(model0b));
        assertFalse(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        Disposable.dispose(model0a);

        assertTrue(Disposable.isDisposed(model0a));
        assertFalse(Disposable.isDisposed(model0b));
        assertTrue(Disposable.isDisposed(model1a));
        assertFalse(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertFalse(Disposable.isDisposed(model2b));

        Disposable.dispose(model0b);

        assertTrue(Disposable.isDisposed(model0a));
        assertTrue(Disposable.isDisposed(model0b));
        assertTrue(Disposable.isDisposed(model1a));
        assertTrue(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertTrue(Disposable.isDisposed(model2b));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model0 {
        private @Nullable String _name;

        static @Nullable Model0 create(final @Nullable String name) {
            return new DualDisposedDependenciesIntegrationTest_Arez_Model0(name);
        }

        Model0(final @Nullable String name) {
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

    // Explicitly requireEquals so there is a stable ordering when disposing by dispose notifier
    @NullMarked
    @ArezComponent(requireEquals = Feature.ENABLE)
    abstract static class Model1 {
        private @Nullable String _name;
        private @Nullable Model0 _leaf;

        static @Nullable Model1 create(final @Nullable String name, final @Nullable Model0 leaf) {
            return new DualDisposedDependenciesIntegrationTest_Arez_Model1(name, leaf);
        }

        Model1(final @Nullable String name, final @Nullable Model0 leaf) {
            _name = name;
            _leaf = leaf;
        }

        @ComponentDependency
        @Observable
        @Nullable
        Model0 getLeaf() {
            return _leaf;
        }

        @Observable
        void setLeaf(final @Nullable Model0 leaf) {
            _leaf = leaf;
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
        private final @Nullable Model0 _leaf;
        private final @Nullable Model1 _reference;
        private @Nullable String _name;

        static @Nullable Model2 create(
                final @Nullable Model1 reference, final @Nullable String name, final @Nullable Model0 leaf) {
            return new DualDisposedDependenciesIntegrationTest_Arez_Model2(reference, name, leaf);
        }

        Model2(final @Nullable Model1 reference, final @Nullable String name, final @Nullable Model0 leaf) {
            _reference = reference;
            _name = name;
            _leaf = leaf;
        }

        @ComponentDependency
        @Nullable
        Model1 getReference() {
            return _reference;
        }

        @ComponentDependency
        @Nullable
        Model0 getLeaf() {
            return _leaf;
        }

        @Observable
        @Nullable
        String getName() {
            return _name;
        }

        void setName(String name) {
            _name = name;
        }
    }
}
