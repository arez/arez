package arez.integration.observable;

import static org.testng.Assert.*;

import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableDeepEqualsComparatorIntegrationTest extends AbstractArezIntegrationTest {
    @NullMarked
    @ArezComponent
    abstract static class Model {
        private int @Nullable [] _values = new int[] {1, 2, 3};

        @Observable(equalityComparator = ObjectsDeepEqualsComparator.class)
        int @Nullable [] getValues() {
            return _values;
        }

        void setValues(final int @Nullable [] values) {
            _values = values;
        }
    }

    @Test
    public void scenario() {
        final Model model = new ObservableDeepEqualsComparatorIntegrationTest_Arez_Model();
        final AtomicInteger callCount = new AtomicInteger();

        observer(() -> {
            model.getValues();
            callCount.incrementAndGet();
        });

        assertEquals(callCount.get(), 1);

        safeAction(() -> model.setValues(new int[] {4, 5, 6}));

        assertEquals(callCount.get(), 2);

        safeAction(() -> model.setValues(new int[] {4, 5, 6}));

        assertEquals(callCount.get(), 2);

        safeAction(() -> model.setValues(new int[] {4, 5, 7}));

        assertEquals(callCount.get(), 3);
    }
}
