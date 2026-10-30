package arez.browser;

import arez.Arez;
import arez.ComputableValue;
import arez.Disposable;
import arez.EqualityComparator;
import arez.Observer;
import arez.ObserverErrorHandler;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.ComputableValueRef;
import arez.annotations.Feature;
import arez.annotations.MappedRelationship;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class MappedRelationshipBrowserTest {
    private static final List<String> CONTEXT = new ArrayList<>();

    private MappedRelationshipBrowserTest() {}

    @JsMethod
    public static void exerciseContextualRelationship() {
        final List<String> errors = new ArrayList<>();
        final ObserverErrorHandler errorHandler = (observer, error, throwable) -> errors.add(error.toString());
        Arez.context().addObserverErrorHandler(errorHandler);
        final var model = new MappedRelationshipBrowserTest_Arez_ContextModel();
        final List<List<ContextRow>> firstResults = new ArrayList<>();
        final List<List<ContextRow>> secondResults = new ArrayList<>();
        final Observer first = observeContext(model, "first", firstResults);
        final Observer second = observeContext(model, "second", secondResults);
        final ContextRow firstRow = firstResults.get(0).get(0);
        final ContextRow secondRow = secondResults.get(0).get(0);
        check(firstRow != secondRow && firstRow._source == secondRow._source, "independent context targets");
        final ComputableValue<List<ContextRow>> firstRef = inContext("first", model::getRowsComputableValue);
        final ComputableValue<List<ContextRow>> secondRef = inContext("second", model::getRowsComputableValue);
        check(firstRef != secondRef, "contextual computation references");
        check(
                inContext("first", model::getDescriptionComputableValue)
                        != inContext("second", model::getDescriptionComputableValue),
                "ordinary contextual computation references");
        Arez.context().safeAction(() -> firstRow.setRank(10));
        check(firstResults.get(firstResults.size() - 1).get(1) == firstRow, "context restoration and identity");
        check(secondResults.get(secondResults.size() - 1).get(0) == secondRow, "independent context state");
        Arez.context().safeAction(() -> {
            firstRow.setPinned(true);
            model.setRevision(1);
        });
        check(firstResults.get(firstResults.size() - 1).contains(firstRow), "contextual absent retention");
        check(Disposable.isDisposed(secondRow), "independent retention decisions");
        first.dispose();
        check(firstRef.isDisposed() && Disposable.isDisposed(firstRow), "context deactivation cleanup");
        check(!secondRef.isDisposed(), "other context remains active");
        Disposable.dispose(model);
        check(secondRef.isDisposed(), "owner releases remaining context");
        second.dispose();
        check(CONTEXT.isEmpty(), "balanced context scopes");
        Arez.context().removeObserverErrorHandler(errorHandler);
        check(errors.isEmpty(), "unexpected observer errors: " + errors);
    }

    private static Observer observeContext(
            final ContextModel model, final String context, final List<List<ContextRow>> results) {
        return Arez.context()
                .observer(
                        () -> {
                            if (Disposable.isNotDisposed(model)) {
                                inContext(context, () -> {
                                    results.add(model.getRows());
                                    check(model.getDescription().startsWith(context), "ordinary memoization context");
                                    return true;
                                });
                            }
                        },
                        Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
    }

    private static <T> T inContext(final String context, final Supplier<T> action) {
        CONTEXT.add(context);
        try {
            return action.get();
        } finally {
            check(CONTEXT.remove(CONTEXT.size() - 1).equals(context), "restore caller context");
        }
    }

    private static String currentContext() {
        return CONTEXT.get(CONTEXT.size() - 1);
    }

    @JsMethod
    public static void exerciseValueKeys() {
        exerciseValueKeysFor(new ValueKey(1), new ValueKey(1), new ValueKey(2));
        exerciseValueKeysFor(Integer.valueOf(1000), Integer.valueOf(1000), Integer.valueOf(2000));
    }

    private static void exerciseValueKeysFor(final Object first, final Object equal, final Object other) {
        check(first != equal && first.equals(equal), "distinct equal source keys");
        final ValueModel model =
                new MappedRelationshipBrowserTest_Arez_ValueModel(Arrays.asList(first, equal, other, null, null));
        final List<List<ValueRow>> results = new ArrayList<>();
        final List<String> errors = new ArrayList<>();
        final ObserverErrorHandler handler = (observer, error, throwable) -> errors.add(error + " " + throwable);
        Arez.context().addObserverErrorHandler(handler);
        try {
            final Observer observer = Arez.context().observer(() -> results.add(model.getRows()));
            final List<ValueRow> initial = results.get(results.size() - 1);
            final ValueRow row = initial.get(0);
            final ValueRow otherRow = initial.get(2);
            final ValueRow blank = initial.get(3);
            check(initial.size() == 5 && model._created.size() == 3, "one factory call per equal key");
            check(row == initial.get(1) && blank == initial.get(4), "equal and null occurrences");
            check(row != otherRow && otherRow._source == other, "unequal keys remain distinct");
            check(row._source == first && blank._source == null, "first encountered construction source");
            Arez.context().safeAction(() -> row.setState(42));
            final int publications = results.size();
            model.replaceSources(Arrays.asList(equal, equal, other, null, null));
            check(results.size() == publications && model._created.size() == 3, "equal keys reuse targets silently");
            check(row._source == first, "reuse does not rebind the original source");
            Arez.context().safeAction(() -> check(row.getState() == 42, "local state survives equal replacement"));

            model.replaceSources(Arrays.asList(equal, other, null));
            check(results.size() == publications + 1, "occurrence changes propagate through custom comparison");
            Arez.context().safeAction(() -> {
                row.setPinned(true);
                blank.setPinned(true);
            });
            model.replaceSources(List.of(other));
            final List<ValueRow> retained = results.get(results.size() - 1);
            check(
                    retained.size() == 3
                            && retained.get(0) == otherRow
                            && retained.get(1) == row
                            && retained.get(2) == blank,
                    "retained keys follow previous encounter order once each");
            Arez.context().safeAction(() -> row.setHidden(true));
            check(results.get(results.size() - 1).size() == 2 && Disposable.isNotDisposed(row), "hidden retained key");
            Arez.context().safeAction(() -> row.setHidden(false));
            model.replaceSources(Arrays.asList(equal, equal, null, other));
            final List<ValueRow> returned = results.get(results.size() - 1);
            check(
                    returned.size() == 4
                            && returned.get(0) == row
                            && returned.get(1) == row
                            && returned.get(2) == blank
                            && returned.get(3) == otherRow,
                    "equal retained keys reappear with their occurrences");
            check(model._created.size() == 3, "reappearance does not construct targets");

            Disposable.dispose(row);
            final List<ValueRow> replaced = results.get(results.size() - 1);
            final ValueRow replacement = replaced.get(0);
            check(replacement != row && replacement.equals(row), "equal disposed target replacement is published");
            check(
                    replacement == replaced.get(1)
                            && replacement._source == equal
                            && Disposable.isNotDisposed(replacement)
                            && model._created.size() == 4,
                    "disposed replacement uses the first current key once");
            model.replaceSources(List.of(other));
            check(Disposable.isDisposed(replacement), "unretained absent target released");
            Arez.context().safeAction(() -> blank.setPinned(false));
            check(
                    Disposable.isDisposed(blank)
                            && results.get(results.size() - 1).size() == 1,
                    "retention release");
            observer.dispose();
            check(model._created.stream().allMatch(Disposable::isDisposed), "value cache deactivation cleanup");
            final Observer reactivated = Arez.context().observer(() -> results.add(model.getRows()));
            check(results.get(results.size() - 1).get(0) != otherRow, "deactivation clears value-key cache");
            Arez.context().safeAction(() -> {
                Disposable.dispose(model);
                reactivated.dispose();
            });
            check(model._created.stream().allMatch(Disposable::isDisposed), "value cache owner disposal");
            check(errors.isEmpty(), "unexpected value-key errors: " + errors);
        } finally {
            Arez.context().removeObserverErrorHandler(handler);
        }
    }

    record ValueKey(int id) {
        @Override
        public int hashCode() {
            return 0;
        }
    }

    @ArezComponent
    abstract static class ValueModel {
        private List<@Nullable Object> _sources;
        private final List<ValueRow> _created = new ArrayList<>();

        ValueModel(final List<@Nullable Object> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @Action
        void replaceSources(final List<@Nullable Object> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        @MappedRelationship(useEqualsForKeys = true, equalityComparator = AlwaysEqual.class)
        abstract List<ValueRow> getRows();

        Stream<@Nullable Object> rowsStream() {
            getRevision();
            return _sources.stream();
        }

        ValueRow createValueRow(final @Nullable Object source) {
            final var row = new MappedRelationshipBrowserTest_Arez_ValueRow(source);
            _created.add(row);
            return row;
        }

        boolean filterValueRow(final ValueRow row) {
            return !row.isHidden();
        }

        boolean retainValueRow(final ValueRow row) {
            return row.isPinned();
        }
    }

    @ArezComponent(disposeOnDeactivate = true)
    abstract static class ValueRow {
        private final @Nullable Object _source;

        ValueRow(final @Nullable Object source) {
            _source = source;
        }

        @Observable
        abstract int getState();

        abstract void setState(int state);

        @Observable
        abstract boolean isHidden();

        abstract void setHidden(boolean hidden);

        @Observable
        abstract boolean isPinned();

        abstract void setPinned(boolean pinned);

        @Override
        public boolean equals(final @Nullable Object object) {
            return object instanceof ValueRow row && Objects.equals(_source, row._source);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(_source);
        }
    }

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

    @ArezComponentLike
    interface ContextAccess {
        @MemoizeContextParameter
        default String captureContext() {
            return currentContext();
        }

        default void pushContext(final String context) {
            CONTEXT.add(context);
        }

        default void popContext(final String context) {
            check(CONTEXT.remove(CONTEXT.size() - 1).equals(context), "restore computation context");
        }
    }

    @ArezComponent(sting = Feature.DISABLE)
    abstract static class ContextModel implements ContextAccess {
        private final List<Source> _sources = List.of(new Source(1), new Source(2));

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @MappedRelationship
        abstract List<ContextRow> getRows();

        @ComputableValueRef
        abstract ComputableValue<List<ContextRow>> getRowsComputableValue();

        @Memoize
        String getDescription() {
            return currentContext() + getRevision();
        }

        @ComputableValueRef
        abstract ComputableValue<String> getDescriptionComputableValue();

        Stream<Source> rowsStream() {
            final String context = currentContext();
            return _sources.stream()
                    .filter(source -> getRevision() == 0 || source.id() == 2)
                    .onClose(() -> check(currentContext().equals(context), "stream context"));
        }

        ContextRow createContextRow(final Source source) {
            return new MappedRelationshipBrowserTest_Arez_ContextRow(source, currentContext(), source.id());
        }

        boolean filterContextRow(final ContextRow row) {
            check(row._context.equals(currentContext()), "filter context");
            return true;
        }

        Comparator<ContextRow> rowsComparator() {
            final String context = currentContext();
            return (first, second) -> {
                check(first._context.equals(context) && second._context.equals(currentContext()), "comparison context");
                return Integer.compare(first.getRank(), second.getRank());
            };
        }

        boolean retainContextRow(final ContextRow row) {
            check(row._context.equals(currentContext()), "retention context");
            return row.isPinned();
        }
    }

    @ArezComponent(sting = Feature.DISABLE, disposeOnDeactivate = true)
    abstract static class ContextRow {
        final Source _source;
        final String _context;

        ContextRow(final Source source, final String context) {
            _source = source;
            _context = context;
        }

        @Observable(initializer = Feature.ENABLE)
        abstract int getRank();

        abstract void setRank(int rank);

        @Observable
        abstract boolean isPinned();

        abstract void setPinned(boolean pinned);
    }

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
