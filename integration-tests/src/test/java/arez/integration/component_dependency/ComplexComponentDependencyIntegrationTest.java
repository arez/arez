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
public final class ComplexComponentDependencyIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final var model1a1 = Model1.create("Model1A1");
        final var model1a2 = Model1.create("Model1A2");
        final var model1a3 = Model1.create("Model1A3");
        final var model1a4 = Model1.create("Model1A4");
        final var model2a = Model2.create(model1a1, model1a2, model1a3, model1a4);

        assertFalse(Disposable.isDisposed(model1a1));
        assertFalse(Disposable.isDisposed(model1a2));
        assertFalse(Disposable.isDisposed(model1a3));
        assertFalse(Disposable.isDisposed(model1a4));
        assertFalse(Disposable.isDisposed(model2a));

        assertNotNull(safeAction(model2a::getReference1));
        Disposable.dispose(model1a1);
        assertNull(safeAction(model2a::getReference1));

        assertTrue(Disposable.isDisposed(model1a1));
        assertFalse(Disposable.isDisposed(model1a2));
        assertFalse(Disposable.isDisposed(model1a3));
        assertFalse(Disposable.isDisposed(model1a4));
        assertFalse(Disposable.isDisposed(model2a));

        Disposable.dispose(model1a3);

        assertTrue(Disposable.isDisposed(model1a1));
        assertFalse(Disposable.isDisposed(model1a2));
        assertTrue(Disposable.isDisposed(model1a3));
        assertFalse(Disposable.isDisposed(model1a4));
        assertTrue(Disposable.isDisposed(model2a));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent
    abstract static class Model1 {
        private @Nullable String _name;

        static Model1 create(final @Nullable String name) {
            return new ComplexComponentDependencyIntegrationTest_Arez_Model1(name);
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
    @SuppressWarnings("Arez:UnmanagedComponentReference")
    abstract static class Model2 {
        private @Nullable Model1 _reference1;
        private @Nullable Model1 _reference2;
        private @Nullable Model1 _reference3;
        private @Nullable Model1 _reference4;

        static Model2 create(
                final @Nullable Model1 reference1,
                final @Nullable Model1 reference2,
                final @Nullable Model1 reference3,
                final @Nullable Model1 reference4) {
            return new ComplexComponentDependencyIntegrationTest_Arez_Model2(
                    reference1, reference2, reference3, reference4);
        }

        Model2(
                final @Nullable Model1 reference1,
                final @Nullable Model1 reference2,
                final @Nullable Model1 reference3,
                final @Nullable Model1 reference4) {
            _reference1 = reference1;
            _reference2 = reference2;
            _reference3 = reference3;
            _reference4 = reference4;
        }

        @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
        @Nullable
        Model1 getReference1() {
            return _reference1;
        }

        @Observable
        void setReference1(final @Nullable Model1 reference) {
            _reference1 = reference;
        }

        @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
        @Nullable
        Model1 getReference2() {
            return _reference2;
        }

        @Observable
        void setReference2(final @Nullable Model1 reference) {
            _reference2 = reference;
        }

        @ComponentDependency(action = ComponentDependency.Action.CASCADE)
        @Nullable
        Model1 getReference3() {
            return _reference3;
        }

        @Observable
        void setReference3(final @Nullable Model1 reference) {
            _reference3 = reference;
        }

        @ComponentDependency(action = ComponentDependency.Action.CASCADE)
        @Nullable
        Model1 getReference4() {
            return _reference4;
        }

        @Observable
        void setReference4(final @Nullable Model1 reference) {
            _reference4 = reference;
        }
    }
}
