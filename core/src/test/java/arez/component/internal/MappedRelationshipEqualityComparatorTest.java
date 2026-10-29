package arez.component.internal;

import static org.testng.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;

public final class MappedRelationshipEqualityComparatorTest {
    @Test
    public void identityOrderAndMultiplicityCannotBeSuppressed() {
        final var calls = new AtomicInteger();
        final var comparator = new MappedRelationshipEqualityComparator((oldValue, newValue) -> {
            calls.incrementAndGet();
            return true;
        });
        final var first = new Value(1);
        final var equal = new Value(1);
        final var second = new Value(2);
        assertFalse(comparator.areEqual(null, List.of(first)));
        assertFalse(comparator.areEqual(List.of(first), null));
        assertFalse(comparator.areEqual(List.of(first), List.of(equal)));
        assertFalse(comparator.areEqual(List.of(first, second), List.of(second, first)));
        assertFalse(comparator.areEqual(List.of(first), List.of(first, first)));
        assertEquals(calls.get(), 0);
        assertTrue(comparator.areEqual(List.of(first, first, second), List.of(first, first, second)));
        assertTrue(comparator.areEqual(null, null));
        assertEquals(calls.get(), 2);
    }

    @Test
    public void additionalComparatorCanForceNotification() {
        final var comparator = new MappedRelationshipEqualityComparator((oldValue, newValue) -> false);
        final var value = new Value(1);
        final var list = List.of(value);
        assertFalse(comparator.areEqual(list, list));
        assertFalse(comparator.areEqual(list, List.of(value)));
        assertFalse(comparator.areEqual(null, null));
    }

    private record Value(int id) {}
}
