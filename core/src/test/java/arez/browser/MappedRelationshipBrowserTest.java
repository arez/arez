package arez.browser;

import arez.Arez;
import arez.Disposable;
import arez.EqualityComparator;
import arez.Observer;
import arez.ObserverErrorHandler;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.MappedRelationship;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class MappedRelationshipBrowserTest {
    private MappedRelationshipBrowserTest() {}

    @JsMethod
    public static void exerciseRelationship() {
        final var first = new Source(1);
        final var equalSource = new Source(1);
        final var model = new MappedRelationshipBrowserTest_Arez_Model(Arrays.asList(first, first, equalSource, null));
        final List<List<Row>> results = new ArrayList<>();
        final List<String> errors = new ArrayList<>();
        final ObserverErrorHandler errorHandler = (observer, error, throwable) -> errors.add(error.toString());
        Arez.context().addObserverErrorHandler(errorHandler);
        final Observer observer = Arez.context().observer(() -> results.add(model.getRows()));
        final List<Row> initial = results.get(results.size() - 1);
        check(initial.size() == 4, "source occurrences");
        final Row blank = initial.get(0);
        final Row firstRow = initial.get(1);
        final Row equalRow = initial.get(3);
        check(blank._source == null, "null source");
        check(firstRow == initial.get(2) && firstRow != equalRow, "identity keys and duplicates");
        Arez.context().safeAction(() -> firstRow.setHidden(true));
        check(results.get(results.size() - 1).size() == 2 && Disposable.isNotDisposed(firstRow), "hidden observation");
        Arez.context().safeAction(() -> firstRow.setHidden(false));
        check(results.get(results.size() - 1).get(1) == firstRow, "hidden target state");
        Arez.context().safeAction(() -> firstRow.setPinned(true));
        model.replaceSources(List.of(equalSource));
        check(
                results.get(results.size() - 1).size() == 2
                        && results.get(results.size() - 1).get(1) == firstRow,
                "absent retention");
        check(Disposable.isDisposed(blank), "absent release");
        Arez.context().safeAction(() -> firstRow.setRank(-2));
        check(results.get(results.size() - 1).get(0) == firstRow, "reactive sorting");
        model.replaceSources(Arrays.asList(first, first, equalSource, null));
        check(
                results.get(results.size() - 1).get(0) == firstRow
                        && results.get(results.size() - 1).get(1) == firstRow,
                "retained reappearance");
        Disposable.dispose(equalRow);
        final Row replacement = results.get(results.size() - 1).get(3);
        check(replacement != equalRow && replacement.equals(equalRow), "guarded equality publishes replacement");
        check(Disposable.isNotDisposed(replacement), "replacement is live");
        final List<Row> finalRows = results.get(results.size() - 1);
        observer.dispose();
        check(finalRows.stream().allMatch(Disposable::isDisposed), "deactivation cleanup");
        Disposable.dispose(model);
        Arez.context().removeObserverErrorHandler(errorHandler);
        check(errors.isEmpty(), "unexpected observer errors: " + errors);
    }

    private static void check(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    record Source(int id) {}

    public static final class AlwaysEqual implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return true;
        }
    }

    @ArezComponent
    abstract static class Model {
        private List<@Nullable Source> _sources;

        Model(final List<@Nullable Source> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @Action
        void replaceSources(final List<@Nullable Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        @MappedRelationship(equalityComparator = AlwaysEqual.class)
        abstract List<Row> getRows();

        Stream<@Nullable Source> rowsStream() {
            getRevision();
            return _sources.stream();
        }

        Row createRow(final @Nullable Source source) {
            return new MappedRelationshipBrowserTest_Arez_Row(source, null == source ? -1 : source.id());
        }

        boolean filterRow(final Row row) {
            return !row.isHidden();
        }

        Comparator<Row> rowsComparator() {
            return Comparator.comparingInt(Row::getRank);
        }

        boolean retainRow(final Row row) {
            return row.isPinned();
        }
    }

    @ArezComponent(disposeOnDeactivate = true)
    abstract static class Row {
        private final @Nullable Source _source;

        Row(final @Nullable Source source) {
            _source = source;
        }

        @Observable(initializer = Feature.ENABLE)
        abstract int getRank();

        abstract void setRank(int rank);

        @Observable
        abstract boolean isHidden();

        abstract void setHidden(boolean hidden);

        @Observable
        abstract boolean isPinned();

        abstract void setPinned(boolean pinned);

        @Override
        public boolean equals(final @Nullable Object object) {
            return object instanceof Row row && Objects.equals(_source, row._source);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(_source);
        }
    }
}
