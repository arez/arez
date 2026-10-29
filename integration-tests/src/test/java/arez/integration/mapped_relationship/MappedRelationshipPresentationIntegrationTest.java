package arez.integration.mapped_relationship;

import static org.testng.Assert.*;

import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.annotations.MappedRelationship;
import arez.annotations.MappedRelationshipComparator;
import arez.annotations.MappedRelationshipFactory;
import arez.annotations.MappedRelationshipFilter;
import arez.annotations.MappedRelationshipRetention;
import arez.annotations.MappedRelationshipSource;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@NullMarked
public final class MappedRelationshipPresentationIntegrationTest extends AbstractArezIntegrationTest {
    @Test
    public void hiddenTargetsKeepTheirStateAndReactiveVisibility() {
        final var source = Source.create(1, "first", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_SortedModel(List.of(source));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row row = results.getLast().get(0);
        safeAction(() -> {
            row.setRank(42);
            row.setVisible(false);
        });
        assertTrue(results.getLast().isEmpty());
        assertFalse(Disposable.isDisposed(row));
        safeAction(() -> row.setVisible(true));
        assertSame(results.getLast().get(0), row);
        assertEquals(safeAction(row::getRank), 42);
        assertEquals(model._creations, 1);
        cleanup(model, observer, source);
    }

    @Test
    public void sourceFilteringPreventsCreationAndParallelStreamsStayTracked() {
        final var source = Source.create(1, "first", false);
        final List<Source> sources = new ArrayList<>();
        sources.add(source);
        for (int i = 2; i <= 16; i++) {
            sources.add(Source.create(i, "excluded", false));
        }
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_SortedModel(sources);
        model._parallel = true;
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        assertTrue(results.getLast().isEmpty());
        assertEquals(model._creations, 0);
        safeAction(() -> source.setIncluded(true));
        final Row original = results.getLast().get(0);
        assertEquals(model._creations, 1);
        safeAction(() -> source.setIncluded(false));
        assertTrue(results.getLast().isEmpty());
        assertTrue(Disposable.isDisposed(original));
        safeAction(() -> source.setIncluded(true));
        assertNotSame(results.getLast().get(0), original);
        assertEquals(model._creations, 2);
        cleanup(model, observer, sources.toArray(new Source[0]));
    }

    @Test
    public void sortingTracksBothProviderAndTargetDependencies() {
        final var first = Source.create(1, "first", true);
        final var second = Source.create(2, "second", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_SortedModel(List.of(second, first));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row firstRow = results.getLast().get(0);
        final Row secondRow = results.getLast().get(1);
        assertEquals(firstRow._id, 1);
        safeAction(() -> secondRow.setRank(-1));
        assertSame(results.getLast().get(0), secondRow);
        safeAction(() -> model.setDescending(true));
        assertSame(results.getLast().get(0), firstRow);
        assertEquals(model._creations, 2);
        assertTrue(model._comparatorCalls >= 3);
        cleanup(model, observer, first, second);
    }

    @Test
    public void absentTargetsAppendInEncounterOrderUntilReleased() {
        final var first = Source.create(1, "first", true);
        final var second = Source.create(2, "second", true);
        final var third = Source.create(3, "third", true);
        final var model =
                new MappedRelationshipPresentationIntegrationTest_Arez_UnsortedModel(List.of(first, second, third));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final List<Row> initial = results.getLast();
        safeAction(() -> {
            initial.get(0).setRetained(true);
            initial.get(1).setRetained(true);
            model.setSources(List.of(third));
        });
        assertSame(results.getLast().get(0), initial.get(2));
        assertSame(results.getLast().get(1), initial.get(0));
        assertSame(results.getLast().get(2), initial.get(1));
        safeAction(() -> initial.get(0).setRetained(false));
        assertEquals(results.getLast().size(), 2);
        assertSame(results.getLast().get(1), initial.get(1));
        assertTrue(Disposable.isDisposed(initial.get(0)));
        safeAction(() -> initial.get(1).setRetained(false));
        assertEquals(results.getLast().size(), 1);
        assertTrue(Disposable.isDisposed(initial.get(1)));
        cleanup(model, observer, first, second, third);
    }

    @Test
    public void retainedTargetsUseTheSamePresentationFilterAndComparator() {
        final var first = Source.create(1, "first", true);
        final var second = Source.create(2, "second", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_SortedModel(List.of(second, first));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row retained = results.getLast().get(0);
        final Row present = results.getLast().get(1);
        safeAction(() -> {
            retained.setRetained(true);
            retained.setVisible(false);
            model.setSources(List.of(second));
        });
        assertEquals(results.getLast().size(), 1);
        assertSame(results.getLast().get(0), present);
        assertFalse(Disposable.isDisposed(retained));
        safeAction(() -> retained.setVisible(true));
        assertSame(results.getLast().get(0), retained);
        assertSame(results.getLast().get(1), present);
        assertEquals(model._creations, 2);
        cleanup(model, observer, first, second);
        assertTrue(Disposable.isDisposed(retained));
    }

    @Test
    public void retainedTargetsSurviveSourceDisposalWithNullableDependency() {
        final var source = Source.create(1, "snapshot", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_UnsortedModel(List.of(source));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row row = results.getLast().get(0);
        safeAction(() -> {
            row.setRetained(true);
            model.setSources(List.of());
            Disposable.dispose(source);
        });
        assertSame(results.getLast().get(0), row);
        assertNull(safeAction(row::getSource));
        assertEquals(row._label, "snapshot");
        assertFalse(Disposable.isDisposed(row));
        safeAction(() -> row.setRetained(false));
        assertTrue(results.getLast().isEmpty());
        assertTrue(Disposable.isDisposed(row));
        cleanup(model, observer, source);
    }

    @Test
    public void disposedAbsentTargetsAreCheckedBeforeRetentionPredicate() {
        final var source = Source.create(1, "first", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_UnsortedModel(List.of(source));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row row = results.getLast().get(0);
        safeAction(() -> {
            row.setRetained(true);
            model.setSources(List.of());
        });
        final int calls = model._retentionCalls;
        Disposable.dispose(row);
        assertTrue(results.getLast().isEmpty());
        assertEquals(model._retentionCalls, calls);
        cleanup(model, observer, source);
    }

    @Test
    public void sourceReappearanceReusesRetainedTargetAndPreservesOccurrences() {
        final var source = Source.create(1, "first", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_UnsortedModel(List.of(source, source));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row row = results.getLast().get(0);
        assertSame(results.getLast().get(1), row);
        safeAction(() -> {
            row.setRetained(true);
            model.setSources(List.of());
        });
        assertEquals(results.getLast().size(), 1);
        assertSame(results.getLast().get(0), row);
        safeAction(() -> model.setSources(List.of(source, source)));
        assertEquals(results.getLast().size(), 2);
        assertSame(results.getLast().get(0), row);
        assertSame(results.getLast().get(1), row);
        assertEquals(model._creations, 1);
        cleanup(model, observer, source);
    }

    @DataProvider
    public Object[][] failureModes() {
        return new Object[][] {{"filter"}, {"comparator"}, {"retention"}};
    }

    @Test(dataProvider = "failureModes")
    public void presentationFailuresPropagateAndRecover(final String failure) {
        captureObserverErrors();
        final var source = Source.create(1, "first", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_SortedModel(List.of(source));
        final List<List<Row>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getRows()));
        final Row original = results.getLast().get(0);
        safeAction(() -> original.setRetained(true));
        model._failure = failure;
        safeAction(() -> model.setSources(failure.equals("retention") ? List.of() : List.of(source)));
        assertFalse(getObserverErrors().isEmpty());
        assertTrue(getObserverErrors().stream().allMatch(message -> message.contains(failure + " failed")));
        model._failure = "";
        safeAction(() -> model.setSources(List.of(source)));
        assertEquals(results.getLast().size(), 1);
        assertFalse(Disposable.isDisposed(results.getLast().get(0)));
        cleanup(model, observer, source);
    }

    @Test
    public void explicitCallbacksBindOnlyTheirNamedRelationship() {
        final var source = Source.create(1, "first", true);
        final var model = new MappedRelationshipPresentationIntegrationTest_Arez_ViewsModel(List.of(source));
        final List<List<Row>> left = new ArrayList<>();
        final List<List<Row>> right = new ArrayList<>();
        final Observer observer = observer(() -> {
            left.add(model.getLeft());
            right.add(model.getRight());
        });
        final Row leftRow = left.getLast().get(0);
        assertTrue(right.getLast().isEmpty());
        safeAction(() -> model.setRightVisible(true));
        final Row rightRow = right.getLast().get(0);
        assertEquals(leftRow._kind, 1);
        assertEquals(rightRow._kind, 2);
        assertNotSame(leftRow, rightRow);
        safeAction(() -> {
            leftRow.setRetained(true);
            rightRow.setRetained(true);
            model.setSources(List.of());
        });
        assertSame(left.getLast().get(0), leftRow);
        assertTrue(right.getLast().isEmpty());
        assertTrue(Disposable.isDisposed(rightRow));
        observer.dispose();
        assertTrue(Disposable.isDisposed(leftRow));
        Disposable.dispose(model);
        Disposable.dispose(source);
    }

    private void cleanup(final BaseModel model, final Observer observer, final Source... sources) {
        observer.dispose();
        Disposable.dispose(model);
        for (final Source source : sources) {
            Disposable.dispose(source);
        }
    }

    @ArezComponentLike
    abstract static class BaseModel {
        private List<Source> _sources;
        int _creations;
        int _retentionCalls;
        boolean _parallel;
        String _failure = "";
        private final Thread _ownerThread = Thread.currentThread();

        BaseModel(final List<Source> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        void setSources(final List<Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        Stream<Source> rowsStream() {
            getRevision();
            return (_parallel ? _sources.parallelStream() : _sources.stream())
                    .peek(source -> assertSame(Thread.currentThread(), _ownerThread))
                    .filter(Source::isIncluded);
        }

        Row createRow(final Source source) {
            _creations++;
            return Row.create(source, 0);
        }

        boolean filterRow(final Row row) {
            if (_failure.equals("filter")) {
                throw new IllegalStateException("filter failed");
            }
            return row.isVisible();
        }

        boolean retainRow(final Row row) {
            _retentionCalls++;
            if (_failure.equals("retention")) {
                throw new IllegalStateException("retention failed");
            }
            return row.isRetained();
        }

        @MappedRelationship
        abstract List<Row> getRows();
    }

    @ArezComponent
    abstract static class SortedModel extends BaseModel {
        int _comparatorCalls;

        SortedModel(final List<Source> sources) {
            super(sources);
        }

        @Observable
        abstract boolean isDescending();

        abstract void setDescending(boolean descending);

        Comparator<Row> rowsComparator() {
            _comparatorCalls++;
            if (_failure.equals("comparator")) {
                throw new IllegalStateException("comparator failed");
            }
            final Comparator<Row> comparator =
                    Comparator.comparingInt(Row::getRank).thenComparingInt(row -> row._id);
            return isDescending() ? comparator.reversed() : comparator;
        }
    }

    @ArezComponent
    abstract static class UnsortedModel extends BaseModel {
        UnsortedModel(final List<Source> sources) {
            super(sources);
        }
    }

    @ArezComponent
    abstract static class ViewsModel {
        private List<Source> _sources;

        ViewsModel(final List<Source> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        @Observable
        abstract boolean isRightVisible();

        abstract void setRightVisible(boolean visible);

        void setSources(final List<Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        @MappedRelationshipSource(name = "left")
        Stream<Source> leftSources() {
            return rightStream();
        }

        Stream<Source> rightStream() {
            getRevision();
            return _sources.stream();
        }

        @MappedRelationshipFactory(name = "left")
        Row buildLeft(final Source source) {
            return Row.create(source, 1);
        }

        @MappedRelationshipFactory(name = "right")
        Row buildRight(final Source source) {
            return Row.create(source, 2);
        }

        @MappedRelationshipFilter(name = "left")
        boolean showLeft(final Row row) {
            return row.isVisible();
        }

        @MappedRelationshipFilter(name = "right")
        boolean showRight(final Row row) {
            return isRightVisible() && row.isVisible();
        }

        @MappedRelationshipComparator(name = "left")
        Comparator<Row> sorting() {
            return Comparator.comparingInt(row -> row._id);
        }

        @MappedRelationshipRetention(name = "left")
        boolean retained(final Row row) {
            return row.isRetained();
        }

        @MappedRelationship
        abstract List<Row> getLeft();

        @MappedRelationship
        abstract List<Row> getRight();
    }

    @ArezComponent
    abstract static class Source {
        final int _id;

        Source(final int id) {
            _id = id;
        }

        static Source create(final int id, final String label, final boolean included) {
            return new MappedRelationshipPresentationIntegrationTest_Arez_Source(id, label, included);
        }

        @Observable
        abstract String getLabel();

        abstract void setLabel(String label);

        @Observable(initializer = Feature.ENABLE)
        abstract boolean isIncluded();

        abstract void setIncluded(boolean included);
    }

    @ArezComponent(disposeOnDeactivate = true)
    abstract static class Row {
        final int _id;
        final String _label;
        final int _kind;

        Row(final int id, final String label, final int kind) {
            _id = id;
            _label = label;
            _kind = kind;
        }

        static Row create(final Source source, final int kind) {
            return new MappedRelationshipPresentationIntegrationTest_Arez_Row(
                    source._id, source.getLabel(), kind, source, true);
        }

        @ComponentDependency(action = ComponentDependency.Action.SET_NULL)
        @Observable(initializer = Feature.ENABLE)
        abstract @Nullable Source getSource();

        abstract void setSource(@Nullable Source source);

        @Observable(initializer = Feature.ENABLE)
        abstract boolean isVisible();

        abstract void setVisible(boolean visible);

        @Observable
        abstract boolean isRetained();

        abstract void setRetained(boolean retained);

        @Observable
        abstract int getRank();

        abstract void setRank(int rank);
    }
}
