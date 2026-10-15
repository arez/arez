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
public final class MultiComponentDependencyIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

        final var sharedModel = SharedModel.create();
        final var model = ModelUnderTest.create(sharedModel, sharedModel, sharedModel);
        safeAction(() -> model.setRef4(sharedModel));

        assertFalse(Disposable.isDisposed(sharedModel));
        assertFalse(Disposable.isDisposed(model));

        Disposable.dispose(sharedModel);

        assertTrue(Disposable.isDisposed(sharedModel));
        assertTrue(Disposable.isDisposed(model));

        assertMatchesFixture(recorder);
    }

    @NullMarked
    @ArezComponent(allowEmpty = true)
    abstract static class SharedModel {

        static SharedModel create() {
            return new MultiComponentDependencyIntegrationTest_Arez_SharedModel();
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class ModelUnderTest {

        @ComponentDependency
        final SharedModel _ref1;

        @Nullable
        @ComponentDependency
        final SharedModel _ref2;

        static ModelUnderTest create(final SharedModel ref1, @Nullable final SharedModel ref2, final SharedModel ref3) {
            return new MultiComponentDependencyIntegrationTest_Arez_ModelUnderTest(ref1, ref2, ref3);
        }

        ModelUnderTest(final SharedModel ref1, @Nullable final SharedModel ref2) {
            assertNotNull(ref1);
            _ref1 = ref1;
            _ref2 = ref2;
        }

        @Observable
        @ComponentDependency
        abstract SharedModel getRef3();

        abstract void setRef3(SharedModel model);

        @Nullable
        @Observable
        @ComponentDependency
        abstract SharedModel getRef4();

        abstract void setRef4(@Nullable SharedModel model);
    }
}
