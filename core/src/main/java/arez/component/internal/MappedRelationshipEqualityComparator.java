package arez.component.internal;

import arez.EqualityComparator;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** Internal equality guard that prevents mapped computations from retaining obsolete targets. */
public final class MappedRelationshipEqualityComparator implements EqualityComparator {
    private final EqualityComparator _comparator;

    /**
     * Creates a comparator that first compares ordered target references.
     *
     * @param comparator the additional comparison applied when target references are unchanged.
     */
    public MappedRelationshipEqualityComparator(final EqualityComparator comparator) {
        _comparator = comparator;
    }

    @Override
    public boolean areEqual(@Nullable final Object oldValue, @Nullable final Object newValue) {
        if (oldValue == newValue) {
            return _comparator.areEqual(oldValue, newValue);
        } else if (null == oldValue || null == newValue) {
            return false;
        }
        final var oldTargets = (List<?>) oldValue;
        final var newTargets = (List<?>) newValue;
        if (oldTargets.size() != newTargets.size()) {
            return false;
        }
        for (int i = 0; i < oldTargets.size(); i++) {
            if (oldTargets.get(i) != newTargets.get(i)) {
                return false;
            }
        }
        return _comparator.areEqual(oldValue, newValue);
    }
}
