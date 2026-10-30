package arez.integration.mapped_relationship;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezTestUtil;
import arez.ComputableValue;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.MappedRelationship;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@NullMarked
public final class MappedRelationshipContextIntegrationTest extends AbstractArezIntegrationTest {
    private static final List<@Nullable Object> CONTEXT = new ArrayList<>();

    @DataProvider
    public Object[][] configurations() {
        return new Object[][] {{false, false}, {false, true}, {true, false}, {true, true}};
    }

    @Test(dataProvider = "configurations")
    public void contextsOwnTheirTargetsAndRetention(final boolean wrapped, final boolean nativeComponents) {
        if (wrapped) {
            ArezTestUtil.makeCollectionPropertiesUnmodifiable();
        } else {
            ArezTestUtil.makeCollectionPropertiesModifiable();
        }
        if (nativeComponents) {
            ArezTestUtil.enableNativeComponents();
        } else {
            ArezTestUtil.disableNativeComponents();
        }
        final var source = new Source(1);
        final var other = new Source(2);
        final var first = range("first", List.of(source, other));
        final var second = range("second", List.of(source, other));
        final var model = new MappedRelationshipContextIntegrationTest_Arez_Model();
        final List<List<Row>> firstResults = new ArrayList<>();
        final List<List<Row>> secondResults = new ArrayList<>();
        final Observer firstObserver = observe(model, first, null, firstResults);
        final Observer secondObserver = observe(model, second, null, secondResults);
        final Row firstRow = firstResults.getLast().getFirst();
        final Row secondRow = secondResults.getLast().getFirst();
        assertSame(firstRow._source, secondRow._source);
        assertNotSame(firstRow, secondRow);
        assertEquals(firstRow._range, "first");
        assertEquals(secondRow._range, "second");
        assertEquals(model._created.size(), 4);

        safeAction(() -> firstRow.setRank(10));
        assertSame(firstResults.getLast().getLast(), firstRow);
        assertSame(secondResults.getLast().getFirst(), secondRow);
        safeAction(() -> firstRow.setHidden(true));
        assertEquals(firstResults.getLast().size(), 1);
        assertFalse(Disposable.isDisposed(firstRow));
        safeAction(() -> firstRow.setHidden(false));
        assertSame(firstResults.getLast().getLast(), firstRow);

        safeAction(() -> {
            firstRow.setPinned(true);
            first.setSources(List.of(other));
        });
        assertSame(firstResults.getLast().getLast(), firstRow);
        assertSame(secondResults.getLast().getFirst(), secondRow);
        safeAction(() -> first.setSources(List.of(source, other)));
        assertSame(firstResults.getLast().getLast(), firstRow);
        safeAction(() -> first.setSources(List.of(other)));
        safeAction(() -> firstRow.setPinned(false));
        assertTrue(Disposable.isDisposed(firstRow));
        assertFalse(Disposable.isDisposed(secondRow));

        firstObserver.dispose();
        assertTrue(firstResults.getLast().stream().allMatch(Disposable::isDisposed));
        assertFalse(Disposable.isDisposed(secondRow));
        final Observer reactivated = observe(model, first, null, firstResults);
        assertNotSame(firstResults.getLast().getFirst(), firstResults.getFirst().getLast());
        Disposable.dispose(model);
        assertTrue(model._created.stream().allMatch(Disposable::isDisposed));
        reactivated.dispose();
        secondObserver.dispose();
        Disposable.dispose(first);
        Disposable.dispose(second);
        assertTrue(CONTEXT.isEmpty());
    }

    @Test
    public void valueKeysRemainIndependentBetweenContexts() {
        final var source = new Source(1);
        final var equal = new Source(1);
        final var first = range("first", List.of(source, equal));
        final var second = range("second", List.of(equal, source));
        final var model = new MappedRelationshipContextIntegrationTest_Arez_ValueModel();
        final List<List<Row>> firstResults = new ArrayList<>();
        final List<List<Row>> secondResults = new ArrayList<>();
        final Observer firstObserver = observe(model, first, null, firstResults);
        final Observer secondObserver = observe(model, second, null, secondResults);
        final Row firstRow = firstResults.getLast().getFirst();
        final Row secondRow = secondResults.getLast().getFirst();
        assertSame(firstResults.getLast().getLast(), firstRow);
        assertSame(secondResults.getLast().getLast(), secondRow);
        assertNotSame(firstRow, secondRow);
        assertSame(firstRow._source, source);
        assertSame(secondRow._source, equal);
        assertEquals(model._created.size(), 2);
        safeAction(() -> {
            firstRow.setPinned(true);
            first.setSources(List.of());
        });
        assertSame(firstResults.getLast().getFirst(), firstRow);
        safeAction(() -> first.setSources(List.of(new Source(1))));
        assertSame(firstResults.getLast().getFirst(), firstRow);
        assertSame(firstRow._source, source);
        assertEquals(model._created.size(), 2);
        firstObserver.dispose();
        assertTrue(Disposable.isDisposed(firstRow));
        assertFalse(Disposable.isDisposed(secondRow));
        Disposable.dispose(model);
        assertTrue(Disposable.isDisposed(secondRow));
        secondObserver.dispose();
        Disposable.dispose(first);
        Disposable.dispose(second);
        assertTrue(CONTEXT.isEmpty());
    }

    @Test
    public void fullContextTupleUsesMemoizeEqualityAndReferencesCaptureContext() {
        final var source = new Source(1);
        final var range = range("range", Arrays.asList(source, null, source));
        final var english = new Language("en");
        final var model = new MappedRelationshipContextIntegrationTest_Arez_Model();
        final ComputableValue<List<Row>> englishRef = inContext(range, english, model::getItemsComputableValue);
        assertSame(inContext(range, new Language("en"), model::getItemsComputableValue), englishRef);
        final ComputableValue<List<Row>> defaultRef = inContext(range, null, model::getItemsComputableValue);
        assertNotSame(defaultRef, englishRef);
        assertTrue(model._created.isEmpty());
        final List<List<Row>> englishResults = new ArrayList<>();
        final List<List<Row>> defaultResults = new ArrayList<>();
        final Observer englishObserver = observe(model, range, english, englishResults);
        final Observer defaultObserver = observe(model, range, null, defaultResults);
        assertSame(englishResults.getLast().get(1), englishResults.getLast().get(2));
        assertNotSame(englishResults.getLast().get(1), defaultResults.getLast().get(1));
        safeAction(englishRef::reportPossiblyChanged);
        assertSame(englishResults.getLast().get(1), model._created.getFirst());

        final List<String> descriptions = new ArrayList<>();
        final Observer descriptionObserver = Arez.context()
                .observer(
                        () -> {
                            if (Disposable.isNotDisposed(model)) {
                                descriptions.add(inContext(range, english, () -> model.describe(7)));
                            }
                        },
                        Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        final ComputableValue<String> descriptionRef =
                inContext(range, english, () -> model.describeComputableValue(7));
        assertSame(inContext(range, new Language("en"), () -> model.describeComputableValue(7)), descriptionRef);
        safeAction(() -> model.setRevision(1));
        assertEquals(descriptions.getLast(), "range/en/7/1");
        safeAction(descriptionRef::reportPossiblyChanged);
        assertTrue(CONTEXT.isEmpty());
        englishObserver.dispose();
        assertTrue(englishRef.isDisposed());
        assertFalse(defaultRef.isDisposed());
        defaultObserver.dispose();
        descriptionObserver.dispose();
        Disposable.dispose(model);
        Disposable.dispose(range);
    }

    @Test
    public void disposingCapturedContextReleasesOnlyItsComputation() {
        final var source = new Source(1);
        final var first = range("first", List.of(source));
        final var second = range("second", List.of(source));
        final var model = new MappedRelationshipContextIntegrationTest_Arez_Model();
        final List<List<Row>> firstResults = new ArrayList<>();
        final List<List<Row>> secondResults = new ArrayList<>();
        final Observer firstObserver = observe(model, first, null, firstResults);
        final Observer secondObserver = observe(model, second, null, secondResults);
        final ComputableValue<List<Row>> firstRef = inContext(first, null, model::getItemsComputableValue);
        final ComputableValue<List<Row>> secondRef = inContext(second, null, model::getItemsComputableValue);
        Disposable.dispose(first);
        assertTrue(firstRef.isDisposed());
        assertTrue(Disposable.isDisposed(firstResults.getLast().getFirst()));
        assertFalse(secondRef.isDisposed());
        assertFalse(Disposable.isDisposed(secondResults.getLast().getFirst()));
        Disposable.dispose(model);
        assertTrue(secondRef.isDisposed());
        assertTrue(Disposable.isDisposed(secondResults.getLast().getFirst()));
        firstObserver.dispose();
        secondObserver.dispose();
        Disposable.dispose(second);
        assertTrue(CONTEXT.isEmpty());
    }

    @DataProvider
    public Object[][] failures() {
        return new Object[][] {
            {"source"},
            {"traversal"},
            {"close"},
            {"factory"},
            {"filter"},
            {"provider"},
            {"compare"},
            {"retention"},
            {"describe"}
        };
    }

    @Test(dataProvider = "failures")
    public void failuresRestoreContextAndPermitRetryAndCleanup(final String failure) {
        captureObserverErrors();
        final var source = new Source(1);
        final var range = range("range", List.of(source, new Source(2)));
        final var model = new MappedRelationshipContextIntegrationTest_Arez_Model();
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observe(model, range, new Language("en"), results);
        final Observer descriptionObserver = Arez.context()
                .observer(
                        () -> {
                            if (Disposable.isNotDisposed(model)) {
                                inContext(range, null, () -> model.describe(1));
                            }
                        },
                        Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
        model._failure = failure;
        safeAction(() -> {
            if (failure.equals("retention")) {
                range.setSources(List.of());
            } else if (failure.equals("factory")) {
                range.setSources(List.of(new Source(4), new Source(3), source));
            }
            model.setRevision(1);
        });
        assertFalse(getObserverErrors().isEmpty());
        assertTrue(getObserverErrors().stream().allMatch(message -> message.contains(failure + " failed")));
        assertTrue(CONTEXT.isEmpty());
        model._failure = "none";
        safeAction(() -> {
            range.setSources(List.of(source));
            model.setRevision(2);
        });
        assertEquals(results.getLast().size(), 1);
        assertFalse(Disposable.isDisposed(results.getLast().getFirst()));
        observer.dispose();
        descriptionObserver.dispose();
        assertTrue(model._created.stream().allMatch(Disposable::isDisposed));
        Disposable.dispose(model);
        Disposable.dispose(range);
        assertTrue(CONTEXT.isEmpty());
    }

    private Observer observe(
            final Base model, final Range range, final @Nullable Language language, final List<List<Row>> results) {
        return Arez.context()
                .observer(
                        () -> {
                            if (Disposable.isNotDisposed(model) && Disposable.isNotDisposed(range)) {
                                inContext(range, language, () -> results.add(model.getItems()));
                            }
                        },
                        Observer.Flags.AREZ_OR_NO_DEPENDENCIES);
    }

    private static Range range(final String name, final List<@Nullable Source> sources) {
        return new MappedRelationshipContextIntegrationTest_Arez_Range(name, sources);
    }

    private static <T> T inContext(final Range range, final @Nullable Language language, final Supplier<T> action) {
        CONTEXT.add(range);
        CONTEXT.add(language);
        try {
            return action.get();
        } finally {
            assertSame(CONTEXT.removeLast(), language);
            assertSame(CONTEXT.removeLast(), range);
        }
    }

    private static Range currentRange() {
        for (final Object value : CONTEXT.reversed()) {
            if (value instanceof final Range range) {
                return range;
            }
        }
        throw new IllegalStateException("No range context");
    }

    private static @Nullable Language currentLanguage() {
        for (final Object value : CONTEXT.reversed()) {
            if (null == value || value instanceof Language) {
                return (Language) value;
            }
        }
        throw new IllegalStateException("No language context");
    }

    record Source(int id) {}

    record Language(String name) {}

    @ArezComponentLike
    interface Contexts {
        @MemoizeContextParameter
        default Range captureRange() {
            return currentRange();
        }

        default void pushRange(final Range range) {
            CONTEXT.add(range);
        }

        default void popRange(final Range range) {
            assertSame(CONTEXT.removeLast(), range);
        }

        @MemoizeContextParameter
        default @Nullable Language captureLanguage() {
            return currentLanguage();
        }

        default void pushLanguage(final @Nullable Language language) {
            CONTEXT.add(language);
        }

        default void popLanguage(final @Nullable Language language) {
            assertSame(CONTEXT.removeLast(), language);
        }
    }

    @ArezComponentLike
    abstract static class Base implements Contexts {
        final List<Row> _created = new ArrayList<>();
        String _failure = "none";

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @MappedRelationship(depType = DepType.AREZ_OR_EXTERNAL)
        abstract List<Row> getItems();

        @ComputableValueRef
        abstract ComputableValue<List<Row>> getItemsComputableValue();

        Stream<@Nullable Source> itemsStream() {
            getRevision();
            failAt("source");
            return currentRange().getSources().stream()
                    .peek(source -> failAt("traversal"))
                    .onClose(() -> failAt("close"));
        }

        Row createRow(final @Nullable Source source) {
            if (null != source && source.id() == 3) {
                failAt("factory");
            }
            final var row = new MappedRelationshipContextIntegrationTest_Arez_Row(
                    source, currentRange()._name, currentLanguage(), null == source ? -1 : source.id());
            _created.add(row);
            return row;
        }

        boolean filterRow(final Row row) {
            checkContext(row);
            failAt("filter");
            return !row.isHidden();
        }

        Comparator<Row> itemsComparator() {
            failAt("provider");
            return (first, second) -> {
                checkContext(first);
                checkContext(second);
                failAt("compare");
                return Integer.compare(first.getRank(), second.getRank());
            };
        }

        boolean retainRow(final Row row) {
            checkContext(row);
            failAt("retention");
            return row.isPinned();
        }

        @Memoize(depType = DepType.AREZ_OR_EXTERNAL)
        String describe(final int suffix) {
            final int revision = getRevision();
            failAt("describe");
            final Language language = currentLanguage();
            return currentRange()._name + "/" + (null == language ? "default" : language.name()) + "/" + suffix + "/"
                    + revision;
        }

        @ComputableValueRef(name = "describe")
        abstract ComputableValue<String> describeComputableValue(int suffix);

        private void checkContext(final Row row) {
            assertEquals(currentRange()._name, row._range);
            assertEquals(currentLanguage(), row._language);
        }

        private void failAt(final String stage) {
            currentRange();
            currentLanguage();
            if (_failure.equals(stage)) {
                throw new IllegalStateException(stage + " failed");
            }
        }
    }

    @ArezComponent(sting = Feature.DISABLE)
    abstract static class Model extends Base {}

    @ArezComponent(sting = Feature.DISABLE)
    abstract static class ValueModel extends Base {
        @Override
        @MappedRelationship(depType = DepType.AREZ_OR_EXTERNAL, useEqualsForKeys = true)
        abstract List<Row> getItems();
    }

    @ArezComponent(sting = Feature.DISABLE, disposeNotifier = Feature.ENABLE)
    abstract static class Range {
        final String _name;
        private List<@Nullable Source> _sources;

        Range(final String name, final List<@Nullable Source> sources) {
            _name = name;
            _sources = sources;
        }

        List<@Nullable Source> getSources() {
            getRevision();
            return _sources;
        }

        void setSources(final List<@Nullable Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);
    }

    @ArezComponent(sting = Feature.DISABLE, disposeOnDeactivate = true)
    abstract static class Row {
        final @Nullable Source _source;
        final String _range;
        final @Nullable Language _language;

        Row(final @Nullable Source source, final String range, final @Nullable Language language) {
            _source = source;
            _range = range;
            _language = language;
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
    }
}
