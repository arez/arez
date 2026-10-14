package arez.integration.component_dependency;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ComponentDependencyFieldIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final Model1 model1a = Model1.create();
        final Model1 model1b = Model1.create();
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

        Disposable.dispose(model1b);

        assertTrue(Disposable.isDisposed(model1a));
        assertTrue(Disposable.isDisposed(model1b));
        assertTrue(Disposable.isDisposed(model2a));
        assertTrue(Disposable.isDisposed(model2b));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class Model1 {
        static @Nullable Model1 create() {
            return new ComponentDependencyFieldIntegrationTest_Arez_Model1();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model2 {
        @ComponentDependency(action = ComponentDependency.Action.CASCADE)
        final @Nullable Model1 _reference;

        static @Nullable Model2 create(final @Nullable Model1 reference) {
            return new ComponentDependencyFieldIntegrationTest_Arez_Model2(reference);
        }

        Model2(final @Nullable Model1 reference) {
            _reference = reference;
        }
    }
}
