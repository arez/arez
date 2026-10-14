package arez.integration.observable;

import static org.testng.Assert.*;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class ObservableCustomEqualityComparatorIntegrationTest extends AbstractArezIntegrationTest {
    @NullMarked
    static final class CaseInsensitiveComparator implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            if (null == oldValue || null == newValue) {
                return oldValue == newValue;
            }
            return oldValue.toString().equalsIgnoreCase(newValue.toString());
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model {

        private String _name = "";

        @Observable(setterAlwaysMutates = false, equalityComparator = CaseInsensitiveComparator.class)
        String getName() {
            return _name;
        }

        void setName(final String name) {
            _name = name.trim();
        }
    }

    @Test
    public void scenario() {
        final Model model = new ObservableCustomEqualityComparatorIntegrationTest_Arez_Model();
        final AtomicInteger callCount = new AtomicInteger();

        observer(() -> {
            model.getName();
            callCount.incrementAndGet();
        });

        assertEquals(callCount.get(), 1);

        safeAction(() -> model.setName("Abc"));

        assertEquals(callCount.get(), 2);

        safeAction(() -> model.setName("aBC"));

        assertEquals(callCount.get(), 2);

        safeAction(() -> model.setName("Abc   "));

        assertEquals(callCount.get(), 2);

        safeAction(() -> model.setName("Def"));

        assertEquals(callCount.get(), 3);
    }
}
