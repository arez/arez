package arez.integration.observable;

import static org.testng.Assert.*;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.Test;

@NullMarked
public final class DefaultEqualityComparatorObservableIntegrationTest extends AbstractArezIntegrationTest {
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
        private @Nullable Name _defaultName = new Name("");
        private @Nullable Name _explicitName = new Name("");

        @Observable(setterAlwaysMutates = false)
        @Nullable
        Name getDefaultName() {
            return _defaultName;
        }

        void setDefaultName(final @Nullable Name defaultName) {
            _defaultName = defaultName;
        }

        @Observable(setterAlwaysMutates = false, equalityComparator = ExactComparator.class)
        @Nullable
        Name getExplicitName() {
            return _explicitName;
        }

        void setExplicitName(final @Nullable Name explicitName) {
            _explicitName = explicitName;
        }
    }

    @Test
    public void scenario() {
        final Model model = new DefaultEqualityComparatorObservableIntegrationTest_Arez_Model();
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

        safeAction(() -> model.setDefaultName(new Name("Abc")));
        safeAction(() -> model.setExplicitName(new Name("Abc")));

        assertEquals(defaultCount.get(), 2);
        assertEquals(explicitCount.get(), 2);

        safeAction(() -> model.setDefaultName(new Name("aBC")));
        safeAction(() -> model.setExplicitName(new Name("aBC")));

        assertEquals(defaultCount.get(), 2);
        assertEquals(explicitCount.get(), 3);
    }
}
