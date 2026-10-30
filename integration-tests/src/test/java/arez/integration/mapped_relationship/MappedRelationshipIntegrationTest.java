package arez.integration.mapped_relationship;

import static org.testng.Assert.*;

import arez.Arez;
import arez.ArezTestUtil;
import arez.ComputableValue;
import arez.Disposable;
import arez.EqualityComparator;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.MappedRelationship;
import arez.annotations.Observable;
import arez.annotations.Priority;
import arez.browser.MappedRelationshipBrowserTest;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@NullMarked
public final class MappedRelationshipIntegrationTest extends AbstractArezIntegrationTest {
    @DataProvider
    public Object[][] apiInvariantModes() {
        return new Object[][] {{true}, {false}};
    }

    @Test(dataProvider = "apiInvariantModes")
    public void validatesRuntimeTypesThroughPackagedComponents(final boolean checkApiInvariants) {
        captureObserverErrors();
        if (!checkApiInvariants) {
            ArezTestUtil.noCheckApiInvariants();
        }
        MappedRelationshipBrowserTest.exerciseRuntimeTypes();
    }

    @Test
    public void preservesSourceIdentityOrderDuplicatesAndNull() {
        final var first = new Source(1);
        final var equal = new Source(1);
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(Arrays.asList(first, equal, null, first));
        final List<List<Target>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getItems()));
        final List<Target> initial = results.get(0);
        assertEquals(model._creations, 3);
        assertSame(initial.get(0)._source, first);
        assertSame(initial.get(1)._source, equal);
        assertNotSame(initial.get(0), initial.get(1));
        assertSame(initial.get(0), initial.get(3));
        assertNull(initial.get(2)._source);

        safeAction(() -> model.setSources(Arrays.asList(null, equal, first, first)));
        final List<Target> reordered = results.get(1);
        assertSame(reordered.get(0), initial.get(2));
        assertSame(reordered.get(1), initial.get(1));
        assertSame(reordered.get(2), initial.get(0));
        assertSame(reordered.get(3), initial.get(0));
        assertEquals(model._creations, 3);

        safeAction(() -> model.setSources(List.of(equal)));
        assertEquals(results.size(), 3);
        assertSame(results.get(2).get(0), initial.get(1));
        assertTrue(Disposable.isDisposed(initial.get(0)));
        assertTrue(Disposable.isDisposed(initial.get(2)));
        observer.dispose();
        assertTrue(Disposable.isDisposed(initial.get(1)));
        Disposable.dispose(model);
    }

    @Test
    public void replacementCannotReturnEqualDisposedTarget() {
        final var source = new Source(1);
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(source));
        final List<List<Target>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getItems()));
        final Target original = results.get(0).get(0);
        Disposable.dispose(original);
        final Target replacement = results.get(results.size() - 1).get(0);
        assertNotSame(replacement, original);
        assertSame(replacement._source, source);
        assertFalse(Disposable.isDisposed(replacement));
        assertEquals(model._creations, 2);

        final var equalSource = new Source(1);
        safeAction(() -> model.setSources(List.of(equalSource)));
        final Target rebound = results.get(results.size() - 1).get(0);
        assertNotSame(rebound, replacement);
        assertSame(rebound._source, equalSource);
        assertTrue(Disposable.isDisposed(replacement));
        observer.dispose();
        Disposable.dispose(model);
    }

    @Test
    public void multipleReadersReleaseOnlyAfterLastObserverAndReactivateFresh() {
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(new Source(1)));
        final List<Target> targets = new ArrayList<>();
        final Observer first = observer(() -> targets.add(model.getItems().get(0)));
        final Observer second = observer(() -> targets.add(model.getItems().get(0)));
        final Target original = targets.get(0);
        assertSame(targets.get(1), original);
        first.dispose();
        assertFalse(Disposable.isDisposed(original));
        second.dispose();
        assertTrue(Disposable.isDisposed(original));
        final Observer third = observer(() -> targets.add(model.getItems().get(0)));
        assertNotSame(targets.get(2), original);
        assertEquals(model._creations, 2);
        safeAction(() -> {
            Disposable.dispose(model);
            third.dispose();
        });
        assertTrue(Disposable.isDisposed(targets.get(2)));
    }

    @Test
    public void independentPropertiesUseIndependentCaches() {
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(new Source(1)));
        final List<Target> targets = new ArrayList<>();
        final Observer observer = observer(() -> {
            targets.add(model.getItems().get(0));
            targets.add(model.getOthers().get(0));
        });
        assertNotSame(targets.get(0), targets.get(1));
        assertEquals(model._creations, 2);
        observer.dispose();
        assertTrue(Disposable.isDisposed(targets.get(0)));
        assertTrue(Disposable.isDisposed(targets.get(1)));
        Disposable.dispose(model);
    }

    @Test
    public void unchangedTargetsSuppressNotificationsUnlessComparatorRequestsThem() {
        final var source = new Source(1);
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(source));
        final List<List<Target>> normal = new ArrayList<>();
        final List<List<Target>> forced = new ArrayList<>();
        final Observer first = observer(() -> normal.add(model.getItems()));
        final Observer second = observer(() -> forced.add(model.getForced()));
        safeAction(() -> model.setSources(List.of(source)));
        assertEquals(normal.size(), 1);
        assertEquals(forced.size(), 2);
        assertSame(forced.get(0).get(0), forced.get(1).get(0));
        assertEquals(model._creations, 2);
        first.dispose();
        second.dispose();
        Disposable.dispose(model);
    }

    @DataProvider
    public Object[][] configurations() {
        final List<Object[]> configurations = new ArrayList<>();
        for (final boolean wrapped : List.of(false, true)) {
            for (final boolean nativeComponents : List.of(false, true)) {
                for (final boolean zones : List.of(false, true)) {
                    configurations.add(new Object[] {wrapped, nativeComponents, zones});
                }
            }
        }
        return configurations.toArray(new Object[0][]);
    }

    @Test(dataProvider = "configurations")
    public void cleanupDoesNotDependOnWrappingOrNativeComponents(
            final boolean wrapped, final boolean nativeComponents, final boolean zones) {
        if (nativeComponents) {
            ArezTestUtil.enableNativeComponents();
        } else {
            ArezTestUtil.disableNativeComponents();
        }
        if (zones) {
            ArezTestUtil.enableZones();
        } else {
            ArezTestUtil.disableZones();
        }
        if (wrapped) {
            ArezTestUtil.makeCollectionPropertiesUnmodifiable();
        } else {
            ArezTestUtil.makeCollectionPropertiesModifiable();
        }
        final List<String> errors = new ArrayList<>();
        Arez.context().addObserverErrorHandler((observer, error, throwable) -> errors.add(error + ": " + throwable));
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(new Source(1)));
        final List<Target> targets = new ArrayList<>();
        final Observer first = observer(() -> targets.add(model.getItems().get(0)));
        first.dispose();
        assertTrue(Disposable.isDisposed(targets.get(0)));
        final Observer second = observer(() -> targets.add(model.getItems().get(0)));
        assertNotSame(targets.get(0), targets.get(1));
        second.dispose();
        assertTrue(Disposable.isDisposed(targets.get(1)));
        Disposable.dispose(model);
        assertEquals(errors, List.of());
    }

    @DataProvider
    public Object[][] failureModes() {
        return new Object[][] {{false}, {true}};
    }

    @Test(dataProvider = "failureModes")
    public void failedEvaluationsReleaseOldTargetsAndRecover(final boolean sourceFailure) {
        captureObserverErrors();
        final var first = new Source(1);
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(first));
        final List<List<Target>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getItems()));
        final Target original = results.get(0).get(0);
        model._failSource = sourceFailure;
        model._failFactoryId = sourceFailure ? -1 : 2;
        safeAction(() -> model.setSources(List.of(new Source(2), first)));
        assertTrue(Disposable.isDisposed(original));
        assertFalse(getObserverErrors().isEmpty());
        final String failure = sourceFailure ? "source failed" : "factory failed";
        assertTrue(getObserverErrors().stream().allMatch(message -> message.contains(failure)));
        assertEquals(model._closes, sourceFailure ? 1 : 2);

        model._failSource = false;
        model._failFactoryId = -1;
        safeAction(() -> model.setSources(List.of(first)));
        final Target recovered = results.get(results.size() - 1).get(0);
        assertNotSame(recovered, original);
        assertSame(recovered._source, first);
        assertFalse(Disposable.isDisposed(recovered));
        assertEquals(model._closes, sourceFailure ? 2 : 3);
        observer.dispose();
        assertTrue(Disposable.isDisposed(recovered));
        Disposable.dispose(model);
    }

    @Test
    public void keepAliveAndExternalInvalidationUseCopiedMemoizeSettings() {
        final var first = new Source(1);
        final var model = new MappedRelationshipIntegrationTest_Arez_ExternalModel(List.of(first));
        final Target original = model.getItems().get(0);
        assertSame(model.getItems().get(0), original);
        assertFalse(Disposable.isDisposed(original));
        final var replacementSource = new Source(2);
        model._sources = List.of(replacementSource);
        model.getItemsComputableValue().reportPossiblyChanged();
        final Target replacement = model.getItems().get(0);
        assertNotSame(replacement, original);
        assertSame(replacement._source, replacementSource);
        assertTrue(Disposable.isDisposed(original));
        Disposable.dispose(model);
        assertTrue(Disposable.isDisposed(replacement));
    }

    @Test
    public void nullSourceEntryCanBeRetainedAndReappearWithDuplicates() {
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(Arrays.asList((Source) null));
        final List<List<Target>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getItems()));
        final Target target = results.getLast().get(0);
        safeAction(() -> {
            target.setRetained(true);
            model.setSources(List.of());
        });
        assertSame(results.getLast().get(0), target);
        safeAction(() -> model.setSources(Arrays.asList(null, null)));
        assertEquals(results.getLast().size(), 2);
        assertSame(results.getLast().get(0), target);
        assertSame(results.getLast().get(1), target);
        safeAction(() -> model.setSources(List.of()));
        assertEquals(results.getLast().size(), 1);
        safeAction(() -> target.setRetained(false));
        assertTrue(results.getLast().isEmpty());
        assertTrue(Disposable.isDisposed(target));
        observer.dispose();
        Disposable.dispose(model);
    }

    @Test
    public void deactivationReleasesTargetsCreatedBeforeALaterFactoryFailure() {
        captureObserverErrors();
        final var model = new MappedRelationshipIntegrationTest_Arez_Model(List.of(new Source(0)));
        final List<List<Target>> results = new ArrayList<>();
        final Observer observer = observer(() -> results.add(model.getItems()));
        final var unpublishedSource = new Source(1);
        model._failFactoryId = 2;
        safeAction(() -> model.setSources(List.of(unpublishedSource, new Source(2))));
        assertEquals(results.size(), 1);
        assertEquals(model._created.size(), 2);
        assertSame(model._created.getLast()._source, unpublishedSource);
        assertFalse(getObserverErrors().isEmpty());
        assertTrue(getObserverErrors().stream().allMatch(message -> message.contains("factory failed")));
        observer.dispose();
        assertTrue(model._created.stream().allMatch(Disposable::isDisposed));
        Disposable.dispose(model);
    }

    @DataProvider
    public Object[][] wrappingModes() {
        return new Object[][] {{false}, {true}};
    }

    @Test(dataProvider = "wrappingModes")
    public void inheritedContainerTypesWrapAndReleaseTargets(final boolean wrapped) {
        if (wrapped) {
            ArezTestUtil.makeCollectionPropertiesUnmodifiable();
        } else {
            ArezTestUtil.makeCollectionPropertiesModifiable();
        }
        final var model = new MappedRelationshipIntegrationTest_Arez_ContainerModel(List.of(new Source(1)));
        final List<List<PersistentTarget>> results = new ArrayList<>();
        final Observer first = observer(() -> results.add(model.getItems()));
        final List<PersistentTarget> initial = results.getLast();
        final PersistentTarget target = initial.get(0);
        if (wrapped) {
            assertThrows(UnsupportedOperationException.class, initial::clear);
        }
        first.dispose();
        assertFalse(Disposable.isDisposed(target));
        final Observer second = observer(() -> results.add(model.getItems()));
        final PersistentTarget replacement = results.getLast().get(0);
        assertNotSame(replacement, target);
        second.dispose();
        Disposable.dispose(model);
        assertFalse(Disposable.isDisposed(replacement));
        Disposable.dispose(target);
        Disposable.dispose(replacement);
    }

    @ArezComponentLike
    abstract static class ContainerBase<L, S> {
        private final Function<List<Source>, S> _stream;

        ContainerBase(final Function<List<Source>, S> stream) {
            _stream = stream;
        }

        @Observable
        abstract List<Source> getSources();

        abstract void setSources(List<Source> sources);

        @MappedRelationship
        abstract L getItems();

        S itemsStream() {
            return _stream.apply(getSources());
        }

        PersistentTarget createPersistentTarget(final Source source) {
            return new MappedRelationshipIntegrationTest_Arez_PersistentTarget(source);
        }
    }

    @ArezComponent
    abstract static class ContainerModel extends ContainerBase<List<PersistentTarget>, Stream<Source>> {
        ContainerModel() {
            super(List::stream);
        }
    }

    @ArezComponent(allowEmpty = true, observable = Feature.ENABLE)
    abstract static class PersistentTarget {
        final Source _source;

        PersistentTarget(final Source source) {
            _source = source;
        }
    }

    record Source(int id) {}

    @ArezComponent
    abstract static class Model {
        int _creations;
        final List<Target> _created = new ArrayList<>();
        int _closes;
        boolean _failSource;
        int _failFactoryId = -1;
        private List<@Nullable Source> _sources;

        Model(final List<@Nullable Source> sources) {
            _sources = sources;
        }

        @Observable
        abstract int getRevision();

        abstract void setRevision(int revision);

        void setSources(final List<@Nullable Source> sources) {
            _sources = sources;
            setRevision(getRevision() + 1);
        }

        Stream<@Nullable Source> itemsStream() {
            getRevision();
            if (_failSource) {
                throw new IllegalStateException("source failed");
            }
            return _sources.stream().onClose(() -> _closes++);
        }

        Stream<@Nullable Source> othersStream() {
            return itemsStream();
        }

        Stream<@Nullable Source> forcedStream() {
            return itemsStream();
        }

        Target createTarget(final @Nullable Source source) {
            if (null != source && source.id() == _failFactoryId) {
                throw new IllegalStateException("factory failed");
            }
            _creations++;
            final var target = new MappedRelationshipIntegrationTest_Arez_Target(source);
            _created.add(target);
            return target;
        }

        boolean retainTarget(final Target target) {
            return target.isRetained();
        }

        String createTarget(final int unrelated) {
            return Integer.toString(unrelated);
        }

        @MappedRelationship(equalityComparator = AlwaysEqual.class)
        abstract List<Target> getItems();

        @MappedRelationship
        abstract List<Target> getOthers();

        @MappedRelationship(equalityComparator = NeverEqual.class)
        abstract List<Target> getForced();

        @ComputableValueRef
        abstract ComputableValue<List<Target>> getItemsComputableValue();
    }

    @ArezComponent
    abstract static class ExternalModel {
        List<Source> _sources;

        ExternalModel(final List<Source> sources) {
            _sources = sources;
        }

        Stream<Source> itemsStream() {
            return _sources.stream();
        }

        Target createTarget(final Source source) {
            return new MappedRelationshipIntegrationTest_Arez_Target(source);
        }

        @MappedRelationship(
                keepAlive = true,
                depType = DepType.AREZ_OR_EXTERNAL,
                writeOutsideTransaction = Feature.ENABLE,
                readOutsideTransaction = Feature.ENABLE,
                priority = Priority.LOW,
                observeLowerPriorityDependencies = true,
                reportResult = false)
        abstract List<Target> getItems();

        @ComputableValueRef
        abstract ComputableValue<List<Target>> getItemsComputableValue();
    }

    @ArezComponent(disposeOnDeactivate = true)
    abstract static class Target {
        final @Nullable Source _source;

        Target(final @Nullable Source source) {
            _source = source;
        }

        @Observable
        abstract int getState();

        abstract void setState(int state);

        @Observable
        abstract boolean isRetained();

        abstract void setRetained(boolean retained);

        @Override
        public boolean equals(final @Nullable Object other) {
            return other instanceof Target target && Objects.equals(_source, target._source);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(_source);
        }
    }

    public static final class AlwaysEqual implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return true;
        }
    }

    public static final class NeverEqual implements EqualityComparator {
        @Override
        public boolean areEqual(final @Nullable Object oldValue, final @Nullable Object newValue) {
            return false;
        }
    }
}
