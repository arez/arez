package arez;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ObserverSuspensionTest extends AbstractTest {
    @DataProvider
    public Object[][] configurations() {
        return new Object[][] {{true, true}, {true, false}, {false, true}, {false, false}};
    }

    @Test(dataProvider = "configurations")
    public void suspensionAcrossConfigurations(final boolean production, final boolean zones) {
        ArezTestUtil.resetConfig(production);
        if (zones) {
            ArezTestUtil.enableZones();
        }
        final ArezContext context = zones ? Arez.createZone().getContext() : Arez.context();
        final var errors = new ArrayList<String>();
        context.addObserverErrorHandler((observer, error, cause) -> errors.add(error + ": " + cause));
        final ObservableValue<Object> source = context.observable();
        final var calls = new AtomicInteger();
        final Observer observer = context.observer(() -> {
            source.reportObserved();
            calls.incrementAndGet();
        });
        final Disposable suspension = observer.suspend();
        context.safeAction(source::reportChanged);
        assertEquals(calls.get(), 1);
        suspension.dispose();
        assertEquals(calls.get(), 2);

        final var notifications = new AtomicInteger();
        final Observer tracker = context.tracker(notifications::incrementAndGet);
        final Disposable trackerSuspension = tracker.suspend();
        context.safeObserve(tracker, source::reportObserved);
        assertTrue(tracker.getDependencies().isEmpty());
        trackerSuspension.dispose();
        assertEquals(notifications.get(), 1);
        observer.dispose();
        tracker.dispose();
        assertTrue(errors.isEmpty(), errors.toString());
    }

    @Test
    public void suspensionsReleaseResourcesAndResumeOnlyAfterLastRelease() {
        final ArezContext context = Arez.context();
        final ObservableValue<Integer> source = context.observable();
        final var value = new AtomicInteger(1);
        final var calls = new ArrayList<Integer>();
        final var activations = new AtomicInteger();
        final var deactivations = new AtomicInteger();
        final ComputableValue<Integer> computed = context.computable(() -> {
            source.reportObserved();
            context.registerHook("source", activations::incrementAndGet, deactivations::incrementAndGet);
            return value.get();
        });
        final Observer observer = context.observer(() -> calls.add(computed.get()));

        assertEquals(calls, List.of(1));
        final Disposable first = observer.suspend();
        final Disposable second = observer.suspend();
        assertTrue(observer.isInactive());
        assertTrue(observer.getDependencies().isEmpty());
        assertTrue(computed.getObserver().isInactive());
        assertEquals(deactivations.get(), 1);

        context.safeAction(() -> {
            value.set(2);
            source.reportChanged();
        });
        first.dispose();
        first.dispose();
        assertTrue(first.isDisposed());
        assertFalse(second.isDisposed());
        assertEquals(calls, List.of(1));

        context.safeAction(() -> {
            value.set(3);
            source.reportChanged();
        });
        second.dispose();
        assertEquals(calls, List.of(1, 3));
        assertEquals(activations.get(), 2);
        assertEquals(deactivations.get(), 1);
        observer.dispose();
        assertEquals(deactivations.get(), 2);
    }

    @Test
    public void suppressesQueuedReactionsAndRespectsSchedulerLocks() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final var calls = new AtomicInteger();
        final var otherCalls = new AtomicInteger();
        final Observer observer = context.observer(() -> {
            source.reportObserved();
            calls.incrementAndGet();
        });
        context.observer(() -> {
            source.reportObserved();
            otherCalls.incrementAndGet();
        });
        final Disposable scheduler = context.pauseScheduler();
        context.safeAction(source::reportChanged);
        final Disposable suspension = observer.suspend();
        scheduler.dispose();
        assertEquals(calls.get(), 1);
        assertEquals(otherCalls.get(), 2);

        final Disposable schedulerAgain = context.pauseScheduler();
        suspension.dispose();
        assertEquals(calls.get(), 1);
        schedulerAgain.dispose();
        assertEquals(calls.get(), 2);
    }

    @Test
    public void suppressesInitialExecutionUntilResumed() {
        final ArezContext context = Arez.context();
        final var calls = new AtomicInteger();
        final Observer observer = context.observer(
                calls::incrementAndGet, Observer.Flags.RUN_LATER | Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final Disposable suspension = observer.suspend();
        context.triggerScheduler();
        assertEquals(calls.get(), 0);
        suspension.dispose();
        assertEquals(calls.get(), 1);
    }

    @Test
    public void manualNotificationsCannotReactivateSuspendedObserver() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final var calls = new AtomicInteger();
        final var notifications = new AtomicInteger();
        final Observer observer = context.observer(
                () -> {
                    source.reportObserved();
                    calls.incrementAndGet();
                },
                notifications::incrementAndGet,
                Observer.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES);
        final Disposable scheduler = context.pauseScheduler();
        context.safeAction(source::reportChanged);
        final Disposable suspension = observer.suspend();
        scheduler.dispose();
        observer.schedule();
        context.safeAction(observer::reportStale);
        assertEquals(calls.get(), 1);
        assertEquals(notifications.get(), 0);
        assertTrue(observer.isInactive());
        suspension.dispose();
        assertEquals(calls.get(), 2);
        assertEquals(notifications.get(), 0);
        context.safeAction(source::reportChanged);
        assertEquals(notifications.get(), 1);
    }

    @DataProvider
    public Object[][] externalExecutions() {
        return new Object[][] {{true, true}, {true, false}, {false, true}, {false, false}};
    }

    @Test(dataProvider = "externalExecutions")
    public void externalObservationsReturnFreshValuesAndReleaseTracking(final boolean safe, final boolean function)
            throws Throwable {
        final ArezContext context = Arez.context();
        final ObservableValue<Integer> source = context.observable();
        final var value = new AtomicInteger(10);
        final var activations = new AtomicInteger();
        final var deactivations = new AtomicInteger();
        final var notifications = new AtomicInteger();
        final Observer observer = context.tracker(notifications::incrementAndGet);
        final SafeFunction<Integer> render = () -> {
            source.reportObserved();
            context.registerHook("render", activations::incrementAndGet, deactivations::incrementAndGet);
            return value.get();
        };
        assertEquals(context.safeObserve(observer, render), Integer.valueOf(10));
        final Disposable suspension = observer.suspend();
        assertEquals(deactivations.get(), 1);

        for (final int next : new int[] {20, 30}) {
            context.safeAction(() -> {
                value.set(next);
                source.reportChanged();
            });
            if (function) {
                final Integer result =
                        safe ? context.safeObserve(observer, render) : context.observe(observer, render::call);
                assertEquals(result, Integer.valueOf(next));
            } else {
                final Procedure observe = () -> assertEquals(render.call(), Integer.valueOf(next));
                if (safe) {
                    context.safeObserve(observer, () -> assertEquals(render.call(), Integer.valueOf(next)));
                } else {
                    context.observe(observer, observe);
                }
            }
            assertTrue(observer.isInactive());
            assertTrue(observer.getDependencies().isEmpty());
            assertEquals(activations.get(), deactivations.get());
            assertEquals(notifications.get(), 0);
        }

        suspension.dispose();
        assertEquals(notifications.get(), 1);
        assertEquals(activations.get(), 3);
        assertEquals(context.safeObserve(observer, render), Integer.valueOf(30));
        context.safeAction(source::reportChanged);
        assertEquals(notifications.get(), 2);
        observer.dispose();
        assertEquals(activations.get(), deactivations.get());
    }

    @Test
    public void failedExternalObservationsReleaseTemporaryResources() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final Observer observer = context.tracker(() -> {});
        final Disposable suspension = observer.suspend();
        final var deactivations = new AtomicInteger();
        final var failure = new Exception("render failed");
        assertSame(
                expectThrows(
                        Exception.class,
                        () -> context.observe(observer, (Function<Object>) () -> {
                            source.reportObserved();
                            context.registerHook("render", null, deactivations::incrementAndGet);
                            throw failure;
                        })),
                failure);
        assertEquals(deactivations.get(), 1);
        assertTrue(observer.getDependencies().isEmpty());
        assertTrue(observer.isInactive());
        suspension.dispose();
    }

    @Test
    public void disposalDuringTemporaryObservationReleasesNewHooks() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final Observer observer =
                context.tracker(() -> {}, Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_ALLOWED);
        final Disposable suspension = observer.suspend();
        final var deactivations = new AtomicInteger();
        context.safeObserve(observer, () -> {
            source.reportObserved();
            context.registerHook("render", null, deactivations::incrementAndGet);
            observer.dispose();
        });
        assertTrue(observer.isDisposed());
        assertEquals(deactivations.get(), 1);
        suspension.dispose();
    }

    @Test
    public void suspensionDuringObservationWaitsForTrackingCompletion() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final var observerRef = new AtomicReference<Observer>();
        final var suspensionRef = new AtomicReference<Disposable>();
        final var calls = new AtomicInteger();
        final var deactivations = new AtomicInteger();
        final Observer observer = context.observer(
                () -> {
                    source.reportObserved();
                    context.registerHook("render", null, deactivations::incrementAndGet);
                    if (1 == calls.incrementAndGet()) {
                        suspensionRef.set(observerRef.get().suspend());
                        assertEquals(deactivations.get(), 0);
                        source.reportObserved();
                    }
                },
                Observer.Flags.RUN_LATER);
        observerRef.set(observer);
        context.triggerScheduler();
        assertEquals(deactivations.get(), 1);
        assertTrue(observer.getDependencies().isEmpty());
        context.safeAction(source::reportChanged);
        assertEquals(calls.get(), 1);
        suspensionRef.get().dispose();
        assertEquals(calls.get(), 2);
        assertEquals(deactivations.get(), 1);
    }

    @Test
    public void suspensionReleasedDuringObservationResumesAfterCleanup() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final var events = new ArrayList<String>();
        final var observerRef = new AtomicReference<Observer>();
        final Observer observer = context.observer(
                () -> {
                    source.reportObserved();
                    context.registerHook("render", () -> events.add("activate"), () -> events.add("deactivate"));
                    events.add("observe");
                    if (2 == events.size()) {
                        observerRef.get().suspend().dispose();
                        events.add("released");
                    }
                },
                Observer.Flags.RUN_LATER);
        observerRef.set(observer);
        context.triggerScheduler();
        assertEquals(events, List.of("activate", "observe", "released", "deactivate", "activate", "observe"));
        assertEquals(observer.getDependencies().size(), 1);
    }

    @Test
    public void externalResumeWaitsForTemporaryObservationCleanup() {
        final ArezContext context = Arez.context();
        final ObservableValue<Object> source = context.observable();
        final var events = new ArrayList<String>();
        final Observer observer = context.tracker(() -> events.add("notify"));
        final Disposable suspension = observer.suspend();
        assertEquals(
                context.safeObserve(observer, () -> {
                    source.reportObserved();
                    context.registerHook("render", null, () -> events.add("deactivate"));
                    suspension.dispose();
                    events.add("observe");
                    return 23;
                }),
                Integer.valueOf(23));
        assertEquals(events, List.of("observe", "deactivate", "notify"));
        assertTrue(observer.getDependencies().isEmpty());
    }

    @Test
    public void resumeRequestsInNestedActionsAreCoalescedAtRootCompletion() {
        final ArezContext context = Arez.context();
        final var calls = new AtomicInteger();
        final Observer observer = context.observer(calls::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final Disposable suspension = observer.suspend();
        context.safeAction(() -> {
            context.safeAction(
                    () -> {
                        suspension.dispose();
                    },
                    ActionFlags.REQUIRE_NEW_TRANSACTION);
            observer.suspend().dispose();
            assertEquals(calls.get(), 1);
        });
        assertEquals(calls.get(), 2);
    }

    @Test
    public void resuspensionBeforeRootCompletionCancelsPendingResume() {
        final ArezContext context = Arez.context();
        final var calls = new AtomicInteger();
        final var nextSuspension = new AtomicReference<Disposable>();
        final Observer observer = context.observer(calls::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final Disposable suspension = observer.suspend();
        context.safeAction(() -> {
            suspension.dispose();
            nextSuspension.set(observer.suspend());
        });
        assertEquals(calls.get(), 1);
        nextSuspension.get().dispose();
        assertEquals(calls.get(), 2);
    }

    @Test
    public void resumeUsesObserverPriorities() {
        final ArezContext context = Arez.context();
        final var calls = new ArrayList<String>();
        final Observer high = context.observer(
                () -> calls.add("high"), Observer.Flags.AREZ_OR_NO_DEPENDENCIES | Observer.Flags.PRIORITY_HIGH);
        final Observer low = context.observer(
                () -> calls.add("low"), Observer.Flags.AREZ_OR_NO_DEPENDENCIES | Observer.Flags.PRIORITY_LOW);
        final Disposable highSuspension = high.suspend();
        final Disposable lowSuspension = low.suspend();
        calls.clear();
        context.safeAction(() -> {
            lowSuspension.dispose();
            highSuspension.dispose();
            assertTrue(calls.isEmpty());
        });
        assertEquals(calls, List.of("high", "low"));
    }

    @Test
    public void disposalWhileSuspendedIsTerminal() {
        final ArezContext context = Arez.context();
        final var calls = new AtomicInteger();
        final Observer observer = context.observer(calls::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final Disposable first = observer.suspend();
        final Disposable second = observer.suspend();
        observer.dispose();
        first.dispose();
        second.dispose();
        second.dispose();
        context.triggerScheduler();
        assertTrue(observer.isDisposed());
        assertTrue(first.isDisposed());
        assertTrue(second.isDisposed());
        assertEquals(calls.get(), 1);
    }

    @Test
    public void disposalBeforeRootCompletionCancelsPendingResume() {
        final ArezContext context = Arez.context();
        final var calls = new AtomicInteger();
        final Observer observer = context.observer(calls::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final Disposable suspension = observer.suspend();
        context.safeAction(() -> {
            suspension.dispose();
            observer.dispose();
        });
        assertEquals(calls.get(), 1);
        assertTrue(observer.isDisposed());
    }

    @Test
    public void cannotSuspendComputableObserver() {
        final String name = ValueUtil.randomString();
        final Observer observer = Arez.context().computable(name, () -> 1).getObserver();
        assertInvariantFailure(
                observer::suspend,
                "Arez-0208: Observer.suspend() invoked on observer named '" + name + "' owned by a ComputableValue.");
    }

    @Test
    public void cannotSuspendDisposedObserver() {
        final String name = ValueUtil.randomString();
        final Observer observer = Arez.context().tracker(name, () -> {});
        observer.dispose();
        assertInvariantFailure(
                observer::suspend,
                "Arez-0217: Observer.suspend() invoked on disposed or disposing observer named '" + name + "'.");
    }
}
