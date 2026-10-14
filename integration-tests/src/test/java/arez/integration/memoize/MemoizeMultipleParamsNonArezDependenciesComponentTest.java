package arez.integration.memoize;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObservableValueInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeMultipleParamsNonArezDependenciesComponentTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final Element element = Element.create("");

        final AtomicInteger observerCallCount = new AtomicInteger();
        observer(() -> {
            observerCallCount.incrementAndGet();
            element.getMemoized(3);
        });

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element.getCallCount(3), 1);

        context.action(() -> assertEquals(element.getMemoized(3), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element.getCallCount(3), 1);

        context.action(() -> element.getMemoizedComputableValue(3).reportPossiblyChanged());

        context.action(() -> assertEquals(element.getMemoized(3), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element.getCallCount(3), 2);

        element._result = "NewValue";

        context.action(() -> element.getMemoizedComputableValue(3).reportPossiblyChanged());

        context.action(() -> assertEquals(element.getMemoized(3), "NewValue"));

        assertEquals(observerCallCount.get(), 2);
        assertEquals(element.getCallCount(3), 3);
    }

    @Test
    public void lazyValidationDoesNotTrackTransitiveParameterizedMemoizeDependencies() throws Throwable {
        final ArezContext context = Arez.context();

        final Element element = Element.create("");

        final AtomicInteger observerCallCount = new AtomicInteger();
        observer(() -> {
            observerCallCount.incrementAndGet();
            assertEquals(element.getOuterMemoized(3), "");
        });

        assertEquals(observerCallCount.get(), 1);
        assertDependencies(
                context, element.getOuterMemoizedComputableValue(3), element.getNestedMemoizedComputableValue(3));
        assertDependencies(context, element.getNestedMemoizedComputableValue(3), element.getMemoizedComputableValue(3));

        context.action(() -> element.getMemoizedComputableValue(3).reportPossiblyChanged());
        context.action(() -> assertEquals(element.getOuterMemoized(3), ""));

        assertEquals(observerCallCount.get(), 1);
        assertDependencies(
                context, element.getOuterMemoizedComputableValue(3), element.getNestedMemoizedComputableValue(3));
        assertDependencies(context, element.getNestedMemoizedComputableValue(3), element.getMemoizedComputableValue(3));
        assertNotObserver(context, element.getMemoizedComputableValue(3), element.getOuterMemoizedComputableValue(3));
    }

    private static void assertDependencies(
            final ArezContext context,
            final ComputableValue<?> computableValue,
            final ComputableValue<?>... dependencies) {
        final List<ObservableValueInfo> actual =
                context.getSpy().asComputableValueInfo(computableValue).getDependencies();
        assertEquals(actual.size(), dependencies.length);
        for (int i = 0; i < dependencies.length; i++) {
            assertEquals(actual.get(i).getName(), dependencies[i].getName());
        }
    }

    private static void assertNotObserver(
            final ArezContext context, final ComputableValue<?> observable, final ComputableValue<?> observer) {
        assertFalse(context.getSpy().asComputableValueInfo(observable).getObservers().stream()
                .anyMatch(o -> o.getName().equals(observer.getName())));
        assertFalse(context.getSpy().asComputableValueInfo(observer).getDependencies().stream()
                .anyMatch(o -> o.getName().equals(observable.getName())));
    }

    @NullMarked
    @SuppressWarnings("SameParameterValue")
    @ArezComponent
    public abstract static class Element {
        private final @Nullable Map<Integer, AtomicInteger> _callCounts = new HashMap<>();
        private final @Nullable Map<Integer, AtomicInteger> _nestedCallCounts = new HashMap<>();
        private final @Nullable Map<Integer, AtomicInteger> _outerCallCounts = new HashMap<>();

        @Nullable
        String _result;

        public static Element create(final String result) {
            return new MemoizeMultipleParamsNonArezDependenciesComponentTest_Arez_Element(result);
        }

        Element(final String result) {
            _result = result;
        }

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        String getMemoized(final int index) {
            _callCounts.computeIfAbsent(index, i -> new AtomicInteger()).incrementAndGet();
            return _result;
        }

        int getCallCount(final int index) {
            return _callCounts.computeIfAbsent(index, i -> new AtomicInteger()).get();
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getMemoizedComputableValue(int index);

        @Memoize
        String getNestedMemoized(final int index) {
            _nestedCallCounts.computeIfAbsent(index, i -> new AtomicInteger()).incrementAndGet();
            return getMemoized(index);
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getNestedMemoizedComputableValue(int index);

        @Memoize
        String getOuterMemoized(final int index) {
            _outerCallCounts.computeIfAbsent(index, i -> new AtomicInteger()).incrementAndGet();
            return getNestedMemoized(index);
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getOuterMemoizedComputableValue(int index);
    }
}
