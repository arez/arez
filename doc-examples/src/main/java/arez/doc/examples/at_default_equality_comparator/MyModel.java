package arez.doc.examples.at_default_equality_comparator;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MyModel {
    @NullMarked
    @DefaultEqualityComparator(TagComparator.class)
    static final class Tag {

        private final String _value;

        Tag(final String value) {
            _value = value;
        }

        @Override
        public String toString() {
            return _value;
        }
    }

    @NullMarked
    static final class TagComparator implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return null == oldValue || null == newValue
                    ? oldValue == newValue
                    : oldValue.toString().equalsIgnoreCase(newValue.toString());
        }
    }

    private Tag _searchTerm = new Tag("");

    @Observable(setterAlwaysMutates = false)
    Tag getSearchTerm() {
        return _searchTerm;
    }

    void setSearchTerm(final Tag searchTerm) {
        _searchTerm = searchTerm;
    }

    @Memoize
    Tag getNormalizedSearchTerm() {
        return new Tag(getSearchTerm().toString().trim());
    }
}
