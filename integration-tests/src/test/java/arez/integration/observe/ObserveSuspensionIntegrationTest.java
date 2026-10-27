package arez.integration.observe;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezTestUtil;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import org.jspecify.annotations.NullMarked;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@NullMarked
public final class ObserveSuspensionIntegrationTest extends AbstractArezIntegrationTest {
    @DataProvider
    public Object[][] componentModes() {
        return new Object[][] {{true}, {false}};
    }

    @Test(dataProvider = "componentModes")
    public void suspendInternalObserverViaRef(final boolean nativeComponents) {
        if (!nativeComponents) {
            ArezTestUtil.disableNativeComponents();
        }
        final InternalView view = new ObserveSuspensionIntegrationTest_Arez_InternalView();
        assertEquals(view._renders, 1);
        final Disposable suspension = view.getRenderObserver().suspend();
        safeAction(() -> view.setValue(7));
        assertEquals(view._renders, 1);
        suspension.dispose();
        assertEquals(view._renders, 2);
        assertEquals(view._lastValue, 7);
        Disposable.dispose(view);
    }

    @Test(dataProvider = "componentModes")
    public void suspendedExternalMethodPreservesArgumentsAndReturnValue(final boolean nativeComponents) {
        if (!nativeComponents) {
            ArezTestUtil.disableNativeComponents();
        }
        final ExternalView view = new ObserveSuspensionIntegrationTest_Arez_ExternalView();
        assertEquals(view.render(1), 1);
        final Disposable suspension = view.getRenderObserver().suspend();
        assertEquals(view._deactivations, 1);
        safeAction(() -> view.setValue(10));
        assertEquals(view.render(5), 15);
        assertEquals(view._activations, 2);
        assertEquals(view._deactivations, 2);
        assertTrue(Arez.context()
                .getSpy()
                .asObserverInfo(view.getRenderObserver())
                .getDependencies()
                .isEmpty());
        assertEquals(view._notifications, 0);
        suspension.dispose();
        assertEquals(view._notifications, 1);
        assertEquals(view._renders, 2);
        assertEquals(view.render(1), 11);
        safeAction(() -> view.setValue(20));
        assertEquals(view._notifications, 2);
        Disposable.dispose(view);
        assertEquals(view._activations, view._deactivations);
    }

    @Test(dataProvider = "componentModes")
    public void temporaryObservationDoesNotKeepReturnedComponentAlive(final boolean nativeComponents) {
        if (!nativeComponents) {
            ArezTestUtil.disableNativeComponents();
        }
        final Resource resource = new ObserveSuspensionIntegrationTest_Arez_Resource();
        final ResourceView view = new ObserveSuspensionIntegrationTest_Arez_ResourceView(resource);
        final Disposable suspension = view.getRenderObserver().suspend();
        assertFalse(Disposable.isDisposed(resource));
        assertSame(view.render(), resource);
        assertTrue(Disposable.isDisposed(resource));
        Disposable.dispose(view);
        suspension.dispose();
    }

    @NullMarked
    @ArezComponent
    abstract static class InternalView {
        int _renders;
        int _lastValue;

        @Observable
        abstract int getValue();

        abstract void setValue(int value);

        @Observe
        void render() {
            _lastValue = getValue();
            _renders++;
        }

        @ObserverRef
        abstract Observer getRenderObserver();
    }

    @NullMarked
    @ArezComponent
    abstract static class ExternalView {
        int _renders;
        int _notifications;
        int _activations;
        int _deactivations;

        @Observable
        abstract int getValue();

        abstract void setValue(int value);

        @Observe(executor = Executor.EXTERNAL)
        int render(final int offset) {
            Arez.context().registerHook("render", () -> _activations++, () -> _deactivations++);
            _renders++;
            return getValue() + offset;
        }

        void onRenderDepsChange() {
            _notifications++;
        }

        @ObserverRef
        abstract Observer getRenderObserver();
    }

    @NullMarked
    @ArezComponent(disposeOnDeactivate = true, allowEmpty = true)
    abstract static class Resource {}

    @NullMarked
    @ArezComponent
    abstract static class ResourceView {
        @SuppressWarnings("Arez:UnmanagedComponentReference")
        private final Resource _resource;

        ResourceView(final Resource resource) {
            _resource = resource;
        }

        @Observe(executor = Executor.EXTERNAL)
        Resource render() {
            ComponentObservable.observe(_resource);
            return _resource;
        }

        void onRenderDepsChange() {}

        @ObserverRef
        abstract Observer getRenderObserver();
    }
}
