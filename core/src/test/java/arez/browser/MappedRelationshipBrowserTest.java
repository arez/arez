package arez.browser;

import arez.Arez;
import arez.Disposable;
import arez.EqualityComparator;
import arez.Observer;
import arez.ObserverErrorHandler;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
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

    @JsMethod
    public static void exerciseRuntimeTypes() {
        final List<String> errors = new ArrayList<>();
        final ObserverErrorHandler handler = (observer, error, throwable) -> errors.add(error + " " + throwable);
        Arez.context().addObserverErrorHandler(handler);
        try {
            final var source = new Source(1);
            final var owner = new MappedRelationshipBrowserTest_Arez_RuntimeOwner(List.of(source));
            final List<List<RuntimeView>> views = new ArrayList<>();
            final List<List<RuntimeBase>> bases = new ArrayList<>();
            final Observer observer = Arez.context().observer(() -> {
                views.add(owner.getViews());
                bases.add(owner.getBases());
            });
            final RuntimeView view = views.get(views.size() - 1).get(0);
            final RuntimeBase base = bases.get(bases.size() - 1).get(0);
            check(view != base, "independent runtime-typed caches");
            Arez.context().safeAction(() -> {
                view.setHidden(true);
                view.setPinned(true);
                base.setHidden(true);
                base.setPinned(true);
            });
            owner.replaceSources(List.of());
            check(
                    views.get(views.size() - 1).isEmpty()
                            && bases.get(bases.size() - 1).isEmpty(),
                    "hidden retained targets");
            check(Disposable.isNotDisposed(view) && Disposable.isNotDisposed(base), "retained targets remain observed");
            Arez.context().safeAction(() -> {
                view.setHidden(false);
                base.setHidden(false);
            });
            check(
                    views.get(views.size() - 1).get(0) == view
                            && bases.get(bases.size() - 1).get(0) == base,
                    "retained visibility and identity");
            owner.replaceSources(List.of(source));
            check(
                    views.get(views.size() - 1).get(0) == view
                            && bases.get(bases.size() - 1).get(0) == base,
                    "runtime-typed reappearance");
            observer.dispose();
            check(Disposable.isDisposed(view) && Disposable.isDisposed(base), "runtime-typed deactivation");
            Disposable.dispose(owner);
            check(errors.isEmpty(), "unexpected runtime-typed errors: " + errors);

            for (final boolean abstractTarget : new boolean[] {false, true}) {
                final RuntimeOwner broken =
                        new MappedRelationshipBrowserTest_Arez_RuntimeOwner(List.of(source, new Source(-1)));
                broken._bad = true;
                broken._hideAll = true;
                final List<List<?>> published = new ArrayList<>();
                final int previousErrors = errors.size();
                final Observer failing = Arez.context()
                        .observer(() -> published.add(abstractTarget ? broken.getBases() : broken.getViews()));
                check(errors.size() > previousErrors && published.isEmpty(), "unobservable target must fail");
                if (Arez.shouldCheckApiInvariants()) {
                    check(errors.get(errors.size() - 1).contains("Arez-0179"), "existing observation invariant");
                }
                check(broken._filterCalls == 0, "hidden invalid targets are checked before filtering");
                check(broken._streamCloses == 1, "failed source traversal closes its stream");
                failing.dispose();
                check(broken._created.stream().allMatch(Disposable::isDisposed), "partial evaluation cleanup");
                broken._bad = false;
                broken._hideAll = false;
                final int failures = errors.size();
                final Observer retry = Arez.context()
                        .observer(() -> published.add(abstractTarget ? broken.getBases() : broken.getViews()));
                check(published.size() == 1 && published.get(0).size() == 2, "retry after failed cache deactivation");
                check(errors.size() == failures, "retry must succeed without observer errors");
                retry.dispose();
                check(broken._created.stream().allMatch(Disposable::isDisposed), "retry cleanup");
                Disposable.dispose(broken);
            }
        } finally {
            Arez.context().removeObserverErrorHandler(handler);
        }
    }

    @ArezComponentLike
    interface RuntimeView {
        boolean isHidden();

        void setHidden(boolean hidden);

        boolean isPinned();

        void setPinned(boolean pinned);
    }

    @ArezComponentLike
    abstract static class RuntimeBase implements RuntimeView {
        @Observable
        public abstract boolean isHidden();

        public abstract void setHidden(boolean hidden);

        @Observable
        public abstract boolean isPinned();

        public abstract void setPinned(boolean pinned);
    }

    @ArezComponent(disposeOnDeactivate = true)
    abstract static class RuntimeRow extends RuntimeBase {}

    private static final class BadRow extends RuntimeBase {
        @Override
        public boolean isHidden() {
            return true;
        }

        @Override
        public void setHidden(final boolean hidden) {}

        @Override
        public boolean isPinned() {
            return false;
        }

        @Override
        public void setPinned(final boolean pinned) {}
    }

    @ArezComponent
    abstract static class RuntimeOwner {
        private List<Source> _sources;
        private final List<RuntimeBase> _created = new ArrayList<>();
        private boolean _bad;
        private boolean _hideAll;
        private int _filterCalls;
        private int _streamCloses;

        RuntimeOwner(final List<Source> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @Action
        void replaceSources(final List<Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        @MappedRelationship(validateTypeAtRuntime = true)
        abstract List<RuntimeView> getViews();

        @MappedRelationship(validateTypeAtRuntime = true)
        abstract List<RuntimeBase> getBases();

        Stream<Source> viewsStream() {
            getRevision();
            return _sources.stream().onClose(() -> _streamCloses++);
        }

        Stream<Source> basesStream() {
            return viewsStream();
        }

        RuntimeView createRuntimeView(final Source source) {
            return createRuntimeBase(source);
        }

        RuntimeBase createRuntimeBase(final Source source) {
            if (_bad && source.id() < 0) {
                return new BadRow();
            }
            final RuntimeBase row = new MappedRelationshipBrowserTest_Arez_RuntimeRow();
            _created.add(row);
            return row;
        }

        boolean filterRuntimeView(final RuntimeView target) {
            _filterCalls++;
            return !_hideAll && !target.isHidden();
        }

        boolean filterRuntimeBase(final RuntimeBase target) {
            return filterRuntimeView(target);
        }

        boolean retainRuntimeView(final RuntimeView target) {
            return target.isPinned();
        }

        boolean retainRuntimeBase(final RuntimeBase target) {
            return retainRuntimeView(target);
        }
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
