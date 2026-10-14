package arez.integration.memoize;

import static org.testng.Assert.*;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class DefaultEqualityComparatorMemoizeIntegrationTest extends AbstractArezIntegrationTest {
    @NullMarked
    @DefaultEqualityComparator(CaseInsensitiveComparator.class)
    static final class Name {
        private final @Nullable String _value;

        Name(final @Nullable String value) {
            _value = value;
        }

        @Override
        public @Nullable String toString() {
            return _value;
        }
    }

    @NullMarked
    static final class CaseInsensitiveComparator implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return null == oldValue || null == newValue
                    ? oldValue == newValue
                    : oldValue.toString().equalsIgnoreCase(newValue.toString());
        }
    }

    @NullMarked
    static final class ExactComparator implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return null == oldValue || null == newValue
                    ? oldValue == newValue
                    : oldValue.toString().equals(newValue.toString());
        }
    }

    @NullMarked
    @ArezComponent
    abstract static class Model {
        private int _value;

        @Observable
        int getValue() {
            return _value;
        }

        void setValue(final int value) {
            _value = value;
        }

        @Memoize
        Name getDefaultName() {
            return new Name(0 == (getValue() % 2) ? "Abc" : "aBC");
        }

        @Memoize(equalityComparator = ExactComparator.class)
        Name getExplicitName() {
            return new Name(0 == (getValue() % 2) ? "Abc" : "aBC");
        }
    }

    @Test
    public void scenario() {
        final Model model = new DefaultEqualityComparatorMemoizeIntegrationTest_Arez_Model();
        final var defaultCount = new AtomicInteger();
        final var explicitCount = new AtomicInteger();

        observer(() -> {
            model.getDefaultName();
            defaultCount.incrementAndGet();
        });
        observer(() -> {
            model.getExplicitName();
            explicitCount.incrementAndGet();
        });

        assertEquals(defaultCount.get(), 1);
        assertEquals(explicitCount.get(), 1);

        safeAction(() -> model.setValue(1));

        assertEquals(defaultCount.get(), 1);
        assertEquals(explicitCount.get(), 2);

        safeAction(() -> model.setValue(2));

        assertEquals(defaultCount.get(), 1);
        assertEquals(explicitCount.get(), 3);
    }
}
