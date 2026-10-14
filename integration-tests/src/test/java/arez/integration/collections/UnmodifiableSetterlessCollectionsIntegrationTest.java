package arez.integration.collections;

import static org.testng.Assert.*;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@NullMarked
@SuppressWarnings({"SimplifiableAssertion", "ExpressionComparedToItself"})
public final class UnmodifiableSetterlessCollectionsIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void scenario() {
        final var m1 = Model1.create();

        // In the future this should actually not throw an exception
        assertThrows(() -> safeAction(() -> assertUnmodifiable(m1.getValue(), ValueUtil::randomString)));

        // Reference identity should be guaranteed
        safeAction(() -> assertTrue(m1.getValue() == m1.getValue()));

        final HashSet<String> value = new HashSet<>(Arrays.asList("1", "2", "3"));

        safeAction(() -> m1.setValue(value));

        // In the future this should actuall no throw an exception
        assertThrows(() -> safeAction(() -> assertUnmodifiable(m1.getValue(), ValueUtil::randomString)));

        // Assert Collections contain expected
        safeAction(() -> assertTrue(m1.getValue().contains(value.iterator().next())));

        // Reference identity should be guaranteed
        safeAction(() -> assertTrue(m1.getValue() == m1.getValue()));
    }

    @Test
    public void scenario_when_CollectionPropertiesModifiable() {
        final var m1 = Model1.create();

        assertThrows(() -> safeAction(() -> assertUnmodifiable(m1.getValue(), ValueUtil::randomString)));

        // Reference identity should be guaranteed
        safeAction(() -> assertTrue(m1.getValue() == m1.getValue()));

        final HashSet<String> value = new HashSet<>(Arrays.asList("1", "2", "3"));

        safeAction(() -> m1.setValue(value));

        assertThrows(() -> safeAction(() -> assertUnmodifiable(m1.getValue(), ValueUtil::randomString)));

        // Assert Collections contain expected
        safeAction(() -> assertTrue(m1.getValue().contains(value.iterator().next())));

        // Reference identity should be guaranteed
        safeAction(() -> assertTrue(m1.getValue() == m1.getValue()));
    }

    private <T extends @Nullable Object> void assertUnmodifiable(
            final Collection<T> collection, final Supplier<T> creator) {
        assertUnsupportedOperation(() -> collection.add(creator.get()));
    }

    private void assertUnsupportedOperation(final @Nullable ThrowingRunnable runnable) {
        assertThrows(UnsupportedOperationException.class, runnable);
    }

    @NullMarked
    @SuppressWarnings("SameParameterValue")
    @ArezComponent
    abstract static class Model1 {

        private Collection<String> _value = new HashSet<>();

        static Model1 create() {
            return new UnmodifiableSetterlessCollectionsIntegrationTest_Arez_Model1();
        }

        @Observable(expectSetter = false)
        Collection<String> getValue() {
            return _value;
        }

        void setValue(final Collection<String> value) {
            getValueObservableValue().preReportChanged();
            _value = value;
            getValueObservableValue().reportChanged();
        }

        @ObservableValueRef
        abstract ObservableValue<Collection<String>> getValueObservableValue();
    }
}
