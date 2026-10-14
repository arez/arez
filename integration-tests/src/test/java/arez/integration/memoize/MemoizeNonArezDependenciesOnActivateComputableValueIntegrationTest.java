package arez.integration.memoize;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class MemoizeNonArezDependenciesOnActivateComputableValueIntegrationTest
        extends AbstractArezIntegrationTest {
    @Test
    public void scenario() throws Throwable {
        final ArezContext context = Arez.context();

        final var element = Element.create("");

        final var observerCallCount = new AtomicInteger();
        observer(() -> {
            observerCallCount.incrementAndGet();
            element.getMemoized();
        });

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 1);
        assertEquals(element._activateCount, 1);
        assertTrue(element.hasMemoizedComputableValue());

        context.action(() -> assertEquals(element.getMemoized(), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 1);
        assertEquals(element._activateCount, 1);

        context.action(element::reportExternalChange);

        context.action(() -> assertEquals(element.getMemoized(), ""));

        assertEquals(observerCallCount.get(), 1);
        assertEquals(element._callCount, 2);
        assertEquals(element._activateCount, 1);

        element._result = "NewValue";

        context.action(element::reportExternalChange);

        context.action(() -> assertEquals(element.getMemoized(), "NewValue"));

        assertEquals(observerCallCount.get(), 2);
        assertEquals(element._callCount, 3);
        assertEquals(element._activateCount, 1);
    }

    @NullMarked
    @ArezComponent
    public abstract static class Element {
        @Nullable
        private ComputableValue<String> _memoizedComputableValue;

        String _result;

        int _callCount;
        int _activateCount;

        public static Element create(final String result) {
            return new MemoizeNonArezDependenciesOnActivateComputableValueIntegrationTest_Arez_Element(result);
        }

        Element(final String result) {
            _result = result;
        }

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        String getMemoized() {
            _callCount++;
            return _result;
        }

        @OnActivate
        void onMemoizedActivate(final ComputableValue<String> computableValue) {
            _activateCount++;
            _memoizedComputableValue = computableValue;
        }

        boolean hasMemoizedComputableValue() {
            return null != _memoizedComputableValue;
        }

        void reportExternalChange() {
            assertNotNull(_memoizedComputableValue);
            assert null != _memoizedComputableValue;
            _memoizedComputableValue.reportPossiblyChanged();
        }
    }
}
