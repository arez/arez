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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeNonArezDependenciesComponentTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final Element element = Element.create("");

        final AtomicInteger observerCallCount = new AtomicInteger();
        observer(() -> {
            observerCallCount.incrementAndGet();
            element.getMemoized();
        });

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 1);

        context.action(() -> assertEquals(element.getMemoized(), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 1);

        context.action(() -> element.getMemoizedComputableValue().reportPossiblyChanged());

        context.action(() -> assertEquals(element.getMemoized(), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 2);

        element._result = "NewValue";

        context.action(() -> element.getMemoizedComputableValue().reportPossiblyChanged());

        context.action(() -> assertEquals(element.getMemoized(), "NewValue"));

        assertEquals(observerCallCount.get(), 2);
        assertEquals(element._callCount, 3);
    }

    @Test
    public void lazyValidationDoesNotTrackTransitiveMemoizeDependencies() throws Throwable {
        final ArezContext context = Arez.context();

        final Element element = Element.create("");

        final AtomicInteger observerCallCount = new AtomicInteger();
        observer(() -> {
            observerCallCount.incrementAndGet();
            assertEquals(element.getOuterMemoized(), "");
        });

        assertEquals(observerCallCount.get(), 1);
        assertDependencies(
                context, element.getOuterMemoizedComputableValue(), element.getNestedMemoizedComputableValue());
        assertDependencies(context, element.getNestedMemoizedComputableValue(), element.getMemoizedComputableValue());

        context.action(() -> element.getMemoizedComputableValue().reportPossiblyChanged());
        context.action(() -> assertEquals(element.getOuterMemoized(), ""));

        assertEquals(observerCallCount.get(), 1);
        assertDependencies(
                context, element.getOuterMemoizedComputableValue(), element.getNestedMemoizedComputableValue());
        assertDependencies(context, element.getNestedMemoizedComputableValue(), element.getMemoizedComputableValue());
        assertNotObserver(context, element.getMemoizedComputableValue(), element.getOuterMemoizedComputableValue());
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
    @ArezComponent
    public abstract static class Element {
        @Nullable
        String _result;

        int _callCount;
        int _nestedCallCount;
        int _outerCallCount;

        public static Element create(final String result) {
            return new MemoizeNonArezDependenciesComponentTest_Arez_Element(result);
        }

        Element(final String result) {
            _result = result;
        }

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        String getMemoized() {
            _callCount++;
            return _result;
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getMemoizedComputableValue();

        @Memoize
        String getNestedMemoized() {
            _nestedCallCount++;
            return getMemoized();
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getNestedMemoizedComputableValue();

        @Memoize
        String getOuterMemoized() {
            _outerCallCount++;
            return getNestedMemoized();
        }

        @ComputableValueRef
        abstract @Nullable ComputableValue<String> getOuterMemoizedComputableValue();
    }
}
