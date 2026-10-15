package arez;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ConfigurationBehaviorTest extends AbstractTest {
    @DataProvider
    public Object[][] configurations() {
        return new Object[][] {
            {true, true, true}, {true, true, false}, {false, false, true}, {false, false, false},
            {true, false, true}, {true, false, false}, {false, true, true}, {false, true, false}
        };
    }

    @Test(dataProvider = "configurations")
    public void reactiveGraphAcrossConfigurations(final boolean invariants, final boolean api, final boolean zones) {
        ArezTestUtil.resetConfig(!invariants && !api);
        if (!invariants) {
            ArezTestUtil.noCheckInvariants();
        }
        if (!api) {
            ArezTestUtil.noCheckApiInvariants();
        }
        if (zones) {
            ArezTestUtil.enableZones();
        }
        final boolean expensiveInvariants = Arez.shouldCheckExpensiveInvariants();
        ArezTestUtil.checkExpensiveInvariants();
        try {
            final ArezContext context = Arez.context();
            final List<String> errors = new ArrayList<>();
            context.addObserverErrorHandler((observer, error, throwable) -> errors.add(error + ": " + throwable));
            final ObservableValue<Integer> left = context.observable();
            final ObservableValue<Integer> right = context.observable();
            final ObservableValue<Boolean> selection = context.observable();
            final var useLeft = new AtomicBoolean(true);
            final var leftValue = new AtomicInteger(1);
            final var rightValue = new AtomicInteger(10);
            final var computations = new AtomicInteger();
            final List<Integer> results = new ArrayList<>();
            final ComputableValue<Integer> derived = context.computable(() -> {
                selection.reportObserved();
                final ObservableValue<Integer> source = useLeft.get() ? left : right;
                source.reportObserved();
                selection.reportObserved();
                source.reportObserved();
                computations.incrementAndGet();
                return (useLeft.get() ? leftValue.get() : rightValue.get()) / 2;
            });
            final Observer observer = context.observer(() -> results.add(derived.get()));
            assertTrue(errors.isEmpty(), errors.toString());
            assertEquals(results, List.of(0));

            context.safeAction(() -> {
                left.reportChanged();
                leftValue.set(2);
            });
            assertEquals(results, List.of(0, 1));

            context.safeAction(() -> {
                left.reportChanged();
                leftValue.set(3);
            });
            assertEquals(results, List.of(0, 1));

            context.safeAction(() -> {
                selection.reportChanged();
                useLeft.set(false);
            });
            assertEquals(results, List.of(0, 1, 5));
            final int count = computations.get();
            context.safeAction(left::reportChanged);
            assertEquals(computations.get(), count);

            context.safeAction(() -> {
                right.reportChanged();
                rightValue.set(20);
            });
            assertEquals(results, List.of(0, 1, 5, 10));
            observer.dispose();
            assertTrue(observer.isDisposed());
            assertFalse(left.hasObservers());
            assertFalse(right.hasObservers());
            assertFalse(selection.hasObservers());
            derived.dispose();
            left.dispose();
            right.dispose();
            selection.dispose();
            assertFalse(context.isTransactionActive());
            assertTrue(errors.isEmpty(), errors.toString());
        } finally {
            if (!expensiveInvariants) {
                ArezTestUtil.noCheckExpensiveInvariants();
            }
        }
    }
}
